package com.god.kahit.Repository;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.god.kahit.Events.AllPlayersReadyEvent;
import com.god.kahit.Events.CategoryVoteResultEvent;
import com.god.kahit.Events.GameJoinedLobbyEvent;
import com.god.kahit.Events.GameLostConnectionEvent;
import com.god.kahit.Events.GameStartedEvent;
import com.god.kahit.Events.LobbyNameChangeEvent;
import com.god.kahit.Events.MyPlayerIdChangedEvent;
import com.god.kahit.Events.NewViewEvent;
import com.god.kahit.Events.PlayerAnsweredQuestionEvent;
import com.god.kahit.Events.PlayerVotedCategoryEvent;
import com.god.kahit.Events.RoomChangeEvent;
import com.god.kahit.Events.TimedOutEvent;
import com.god.kahit.databaseService.ItemDataLoaderRealtime;
import com.god.kahit.databaseService.QuestionDataLoaderRealtime;
import com.god.kahit.model.Category;
import com.god.kahit.model.Item;
import com.god.kahit.model.ItemFactory;
import com.god.kahit.model.Player;
import com.god.kahit.model.Question;
import com.god.kahit.model.QuestionFactory;
import com.god.kahit.model.QuizGame;
import com.god.kahit.model.QuizListener;
import com.god.kahit.networkManager.Callbacks.ClientRequestsCallback;
import com.god.kahit.networkManager.Callbacks.HostEventCallback;
import com.god.kahit.networkManager.Callbacks.NetworkCallback;
import com.god.kahit.networkManager.Connection;
import com.god.kahit.networkManager.ConnectionState;
import com.god.kahit.networkManager.ConnectionType;
import com.god.kahit.networkManager.NetworkManager;
import com.god.kahit.networkManager.NetworkModule;
import com.god.kahit.networkManager.PacketHandler;
import com.god.kahit.view.AfterQuestionScorePageView;
import com.god.kahit.view.CategoryView;
import com.god.kahit.view.LotteryView;
import com.god.kahit.view.QuestionView;
import com.god.kahit.view.ScorePageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;

import static com.god.kahit.model.QuizGame.BUS;

public class Repository { //todo implement a strategy pattern, as we got two different states, host & non-host
    private static final String TAG = Repository.class.getSimpleName();
    private static Repository instance;
    //TODO remove player when done testing
    Player p = new Player("anas", "123");
    private QuizGame quizGame;
    private AppLifecycleHandler appLifecycleHandler;
    private NetworkManager networkManager;
    private PacketHandler packetHandler;
    private AudioHandler audioHandler;

    private Repository() {
    }

    public static Repository getInstance() {
        if (instance == null) {
            instance = new Repository();
        }
        return instance;
    }

    public void setupAudioHandler(Context context) {
        if (audioHandler == null) {
            audioHandler = new AudioHandler(context);
        }
    }

    public void startMusic() {
        audioHandler.startMusic();
    }

    public void stopMusic() {
        audioHandler.stopMusic();
    }

    public void resumeMusic() {
        audioHandler.resumeMusic();
    }

    public void pauseMusic() {
        audioHandler.pauseMusic();
    }

    public void setupAppLifecycleObserver(final Context context) {
        if (appLifecycleHandler == null) {
            Log.i(TAG, "setupAppLifecycleObserver: called");
            appLifecycleHandler = new AppLifecycleHandler(context, new AppLifecycleCallback() {
                @Override
                public void onAppForegrounded() {
                    resumeMusic();
                }

                @Override
                public void onAppBackgrounded() {
                    pauseMusic();
                }

                @Override
                public void onAppTimedOut() {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            clearConnections();
                            BUS.post(new TimedOutEvent());
                        }
                    });
                }
            });
        }
    }

    public void setupNetwork(Context context, final boolean isHost) { //Marking a parameter as final prohibits the reassignment of the parameter within the code block of the method
        networkManager = NetworkModule.getInstance(context, new NetworkCallback() {
            @Override
            public void onBytePayloadReceived(@NonNull String id, @NonNull byte[] receivedBytes) {
                packetHandler.handleReceivedPayload(id, receivedBytes);
            }

            @Override
            public void onHostFound(@NonNull String id, @NonNull Connection connection) {
                fireRoomChangeEvent();
            }

            @Override
            public void onHostLost(@NonNull String id) {
                //todo how to implement? As this is same as disconnected, which is valid connection
            }

            @Override
            public void onClientFound(@NonNull String id, @NonNull Connection connection) {
                //Do nothing, as only clients can initiate a connection
            }

            @Override
            public void onConnectionEstablished(@NonNull String id, @NonNull Connection connection) {
                setupPacketHandler();
                if (networkManager.isHost()) {
                    packetHandler.sendPlayerId(connection, connection.getId()); //Let client know their id
                    quizGame.addNewPlayerToEmptyTeam(connection.getName(), connection.getId());
                    packetHandler.broadcastPlayerJoined(connection.getId(), connection.getName());
                    packetHandler.broadcastPlayerChangeTeam(connection.getId(), quizGame.getPlayerTeam(connection.getId()).getId());
                } else {
                    pauseNetInCommunication(); //Pause in-coming communication, letting client set up needed logic beforehand.
                    packetHandler.sendPlayerId(connection, connection.getId()); //Let host let their id
                    BUS.post(new GameJoinedLobbyEvent());
                }
            }

            @Override
            public void onConnectionLost(@NonNull String id) {
//                if (isHost && !quizGame.hasGameStarted()) { //todo How do we deal with mid-game disconnects, simply ignore them? What about waiting for everyone to send ready-packet?
                if (isHost) { //todo How do we deal with mid-game disconnects, simply ignore them? What about waiting for everyone to send ready-packet?
                    //Remove player completely only if game is in lobby
                    Player player = quizGame.getPlayer(id);
                    quizGame.removePlayer(player);
                    if (packetHandler != null) {
                        packetHandler.broadcastPlayerLeft(id);
                    } else {
                        Log.i(TAG, "onConnectionLost: attempt to call broadcastPlayerLeft on null packetHandler, ignoring call");
                    }
                } else if (!isHost) {
                    BUS.post(new GameLostConnectionEvent()); //todo implement
                }
            }

            @Override
            public void onConnectionChanged(@NonNull Connection connection, @NonNull ConnectionState oldState, @NonNull ConnectionState newState) {
                if (!isHost && networkManager.isScanning()) {
                    fireRoomChangeEvent();
                }
                fireTeamChangeEvent();
            }
        });
    }

    private void setupPacketHandler() {
        if (networkManager.isHost()) {
            packetHandler = new PacketHandler(networkManager, new ClientRequestsCallback() {
                @Override
                public void onReceivedMyConnectionId(@NonNull String senderId, @NonNull String playerId) {
                    Log.i(TAG, String.format("onReceivedMyConnectionId: event triggered. Old hostPlayerId: '%s', new hostPlayerId: '%s'", quizGame.getHostPlayerId(), playerId));
                    getHostPlayer().setId(playerId);
                    quizGame.setHostPlayerId(playerId);

                    handleLobbySyncProcedure(senderId);

                    BUS.post(new MyPlayerIdChangedEvent(playerId));
                }

                @Override
                public void onPlayerNameChangeRequest(@NonNull String targetPlayerId, @NonNull String newName) {
                    Log.i(TAG, String.format("onPlayerNameChangeRequest: event triggered. callback from: '%s', new name: '%s'", targetPlayerId, newName));
                    packetHandler.broadcastPlayerNameChange(targetPlayerId, newName); //todo only pass to quizgame, let it trigger a broadcast
                }

                @Override
                public void onPlayerReadyChangeRequest(@NonNull String targetPlayerId, @NonNull boolean newState) {
                    Log.i(TAG, String.format("onPlayerReadyStateChangeRequest: event triggered. callback from: '%s', new state: '%s'", targetPlayerId, String.valueOf(newState)));
                    Player targetPlayer = quizGame.getPlayer(targetPlayerId);
                    if (targetPlayer != null) {
                        setPlayerReady(targetPlayer, newState);
                        packetHandler.broadcastPlayerReadyChange(targetPlayerId, newState);  //todo only pass to quizGame, let it trigger a broadcast
                    } else {
                        Log.i(TAG, String.format("onPlayerReadyStateChangeRequest: targetPlayer was not found. targetPlayerId: '%s', new state: '%s' - ignoring request", targetPlayerId, newState));
                    }
                }

                @Override
                public void onTeamNameChangeRequest(@NonNull String teamId, @NonNull String newTeamName) {
                    Log.i(TAG, String.format("onTeamNameChangeRequest: event triggered. teamId: '%s', new team name: '%s'", teamId, newTeamName));
                    packetHandler.broadcastTeamNameChange(teamId, newTeamName);  //todo only pass to quizGame, let it trigger a broadcast
                }

                @Override
                public void onPlayerTeamChangeRequest(@NonNull String targetPlayerId, @NonNull String newTeamId) {
                    Log.i(TAG, String.format("onPlayerTeamChangeRequest: event triggered. targetPlayerId: '%s', newTeamId: '%s'", targetPlayerId, newTeamId));
                    quizGame.changeTeam(quizGame.getPlayer(targetPlayerId), newTeamId);
                    packetHandler.broadcastPlayerChangeTeam(targetPlayerId, newTeamId);
                }

                @Override
                public void onCategoryPlayerVoteRequest(@NonNull String targetPlayerId, @NonNull String categoryId) {
                    Log.i(TAG, String.format("onCategoryPlayerVoteRequest: event triggered. targetPlayerId: '%s', categoryId: '%s'", targetPlayerId, categoryId));
                    Player player = quizGame.getPlayer(targetPlayerId);
                    if (player != null) {
                        packetHandler.broadcastCategoryPlayerVote(targetPlayerId, categoryId);
                        BUS.post(new PlayerVotedCategoryEvent(player, categoryId));
                    } else {
                        Log.i(TAG, String.format("onCategoryPlayerVoteRequest: Target player (id: '%s') does not exist in quizGame! Skipping player vote category request", targetPlayerId));
                    }
                }

                @Override
                public void onPlayerAnsweredQuestionRequest(@NonNull String targetPlayerId, @NonNull String categoryId, @NonNull String questionId, @NonNull String givenAnswer, long timeLeft) {
                    Log.i(TAG, String.format("onPlayerAnsweredQuestionRequest: event triggered. " +
                                    "targetPlayerId: '%s', categoryId: '%s', questionId: '%s', " +
                                    "givenAnswer: '%s', timeLeft: '%s'", targetPlayerId, categoryId,
                            questionId, givenAnswer, Long.toString(timeLeft)));

                    Player player = quizGame.getPlayer(targetPlayerId);
                    quizGame.setCurrentCategory(categoryId); //todo find a better solution
                    Category category = quizGame.getCurrentCategory(); //Is always synced at this point
                    Question question = quizGame.getQuestion(category, Integer.valueOf(questionId));

                    quizGame.enterAnswer(player, givenAnswer, question, timeLeft);
                    packetHandler.broadcastPlayerAnsweredQuestion(player.getId(), categoryId, questionId, givenAnswer, Long.toString(timeLeft));
                    BUS.post(new PlayerAnsweredQuestionEvent(player, givenAnswer));
                }
            });
        } else {
            packetHandler = new PacketHandler(networkManager, new HostEventCallback() {
                @Override
                public void onReceivedMyConnectionId(@NonNull String playerId) {
                    Log.i(TAG, String.format("onReceivedMyConnectionId: event triggered. received playerId: '%s'", playerId));
                    BUS.post(new MyPlayerIdChangedEvent(playerId));
                }

                @Override
                public void onPlayerNameChangeEvent(@NonNull String targetId, @NonNull String newName) {
                    Log.i(TAG, String.format("onPlayerNameChangeEvent: event triggered. targetId: '%s', new name: '%s'", targetId, newName));
                    if (networkManager.isMe(targetId)) {
                        Log.i(TAG, String.format("onPlayerNameChangeEvent: Target was me! Changing my playername to: '%s'", newName));
                        networkManager.setPlayerName(newName);
                    }
                    Player player = quizGame.getPlayer(targetId);
                    if (player != null) {
                        player.setName(newName);
                        fireTeamChangeEvent();
                    } else {
                        Log.i(TAG, String.format("onPlayerNameChangeEvent: Target player (id: '%s') does not exist in quizGame! Skipping player name change event", targetId));
                    }
                }

                @Override
                public void onPlayerReadyChangeEvent(@NonNull String targetId, @NonNull boolean newState) {
                    Log.i(TAG, String.format("onPlayerReadyChangeEvent: event triggered. targetId: '%s', new state: '%s'", targetId, newState));
                    Player player = quizGame.getPlayer(targetId);
                    if (player != null) {
                        setPlayerReady(player, newState);
                    } else {
                        Log.i(TAG, String.format("onPlayerReadyChangeEvent: Target player (id: '%s') does not exist in quizGame! Skipping ready change event", targetId));
                    }
                }

                @Override
                public void onTeamNameChangeEvent(@NonNull String teamId, @NonNull String newTeamName) {
                    Log.i(TAG, String.format("onTeamNameChangeEvent: event triggered. teamId: '%s', new team name: '%s'", teamId, newTeamName));
                    //todo implement onTeamNameChangeEvent
                }

                @Override
                public void onPlayerJoinedEvent(@NonNull String playerId, @NonNull String playerName) {
                    Log.i(TAG, String.format("onPlayerJoinedEvent: event triggered. playerId: '%s', player name: '%s'", playerId, playerName));
                    quizGame.addNewPlayerToEmptyTeam(playerName, playerId);
                    //todo as client also construct a connection, so we can keep track of connectionState and color rows accordingly
                }

                @Override
                public void onPlayerLeftEvent(@NonNull String playerId) {
                    Log.i(TAG, String.format("onPlayerLeftEvent: event triggered. playerId: '%s'", playerId));
                    quizGame.removePlayer(quizGame.getPlayer(playerId));
                }

                @Override
                public void onPlayerChangeTeamEvent(@NonNull String targetPlayerId, @NonNull String newTeamId) {
                    Log.i(TAG, String.format("onPlayerChangeTeamEvent: event triggered. targetPlayerId: '%s', newTeamId: %s", targetPlayerId, newTeamId));
                    quizGame.changeTeam(quizGame.getPlayer(targetPlayerId), newTeamId);
                }

                @Override
                public void onTeamCreatedEvent(@NonNull String newTeamId, @NonNull String newTeamName) {
                    Log.i(TAG, String.format("onTeamCreatedEvent: event triggered. newTeamId: '%s', newTeamName: %s", newTeamId, newTeamName));
                    //todo implement onTeamCreatedEvent
                }

                @Override
                public void onTeamDeletedEvent(@NonNull String teamId) {
                    Log.i(TAG, String.format("onTeamDeletedEvent: event triggered. teamId: '%s'", teamId));
                    //todo implement onTeamDeletedEvent
                }

                @Override
                public void onGameStartedEvent() {
                    Log.i(TAG, "onGameStartedEvent: event triggered.");
                    quizGame.resetPlayerReady();
                    quizGame.startGame();
                    quizGame.startRound();
                    BUS.post(new GameStartedEvent());
                }

                @Override
                public void onLobbySyncStartEvent(@NonNull String targetPlayerId, @NonNull String roomName, @NonNull String gameModeId) {
                    Log.i(TAG, "onLobbySyncStartEvent: event triggered.");
                    if (networkManager.isMe(targetPlayerId)) {
                        BUS.post(new LobbyNameChangeEvent(networkManager.getConnectionHost().getName()));
                    } else {
                        pauseNetInCommunication();
                    }
                }

                @Override
                public void onLobbySyncEndEvent() {
                    Log.i(TAG, "onLobbySyncEndEvent: event triggered.");
                    clearNetInCommunicationQueue();
                }

                @Override
                public void onShowQuestionEvent(@NonNull String categoryId, @NonNull String questionId) {
                    Log.i(TAG, String.format("onShowQuestionEvent: event triggered. categoryId: '%s', questionId: '%s'", categoryId, questionId));
                    quizGame.setNextQuestion(categoryId, questionId);
                    BUS.post(new NewViewEvent(QuestionView.class));
                }

                @Override
                public void onShowRoundStatsEvent() {
                    Log.i(TAG, "onShowRoundStatsEvent: event triggered.");
                    BUS.post(new NewViewEvent(AfterQuestionScorePageView.class));
                }

                @Override
                public void onShowCategorySelectionEvent(@NonNull String[] categoryIds) {
                    Log.i(TAG, String.format("onShowCategorySelectionEvent: event triggered. categoryIds: '%s'", Arrays.toString(categoryIds)));
                    quizGame.setCategorySelectionArray(categoryIds);
                    BUS.post(new NewViewEvent(CategoryView.class));
                }

                @Override
                public void onShowLotteryEvent(@NonNull String[][] playersWonItemsMatrix) {
                    Log.i(TAG, String.format("onShowLotteryEvent: event triggered. playersWonItemsMatrix: '%s'", Arrays.toString(playersWonItemsMatrix))); //todo show actual values of matrix
                    BUS.post(new NewViewEvent(LotteryView.class));
                    //todo implement onShowLotteryEvent
                }

                @Override
                public void onShowGameResultsEvent() {
                    Log.i(TAG, "onShowGameResultsEvent: event triggered.");
                    BUS.post(new NewViewEvent(ScorePageView.class));
                    //todo implement onShowGameResultsEvent
                }

                @Override
                public void onCategoryPlayerVoteEvent(@NonNull String targetPlayerId, @NonNull String categoryId) {
                    Log.i(TAG, String.format("onCategoryPlayerVoteEvent: event triggered. targetPlayerId: '%s', categoryId: '%s'", targetPlayerId, categoryId));
                    Player player = quizGame.getPlayer(targetPlayerId);
                    if (player != null) {
                        BUS.post(new PlayerVotedCategoryEvent(player, categoryId));
                    } else {
                        Log.i(TAG, String.format("onCategoryPlayerVoteEvent: Target player (id: '%s') does not exist in quizGame! Skipping player vote category event", targetPlayerId));
                    }
                }

                @Override
                public void onCategoryVoteResultEvent(@NonNull String categoryId) {
                    Log.i(TAG, String.format("onCategoryVoteResultEvent: event triggered. categoryId: '%s'", categoryId));
                    BUS.post(new CategoryVoteResultEvent(categoryId));
                }

                @Override
                public void onPlayerAnsweredQuestionEvent(@NonNull String targetPlayerId, @NonNull String categoryId, @NonNull String questionId, @NonNull String givenAnswer, long timeLeft) {
                    Log.i(TAG, String.format("onPlayerAnsweredQuestionEvent: event triggered. " +
                                    "targetPlayerId: '%s', categoryId: '%s', questionId: '%s', " +
                                    "givenAnswer: '%s', timeLeft: '%s'", targetPlayerId, categoryId,
                            questionId, givenAnswer, Long.toString(timeLeft)));

                    Player player = quizGame.getPlayer(targetPlayerId);
                    Category category = quizGame.getCurrentCategory(); //Is always synced at this point
                    Question question = quizGame.getQuestion(category, Integer.valueOf(questionId));

                    quizGame.enterAnswer(player, givenAnswer, question, timeLeft);
                    BUS.post(new PlayerAnsweredQuestionEvent(player, givenAnswer));
                }
            });
        }
    }

    public void startNewGameInstance(Context context) {
        QuestionFactory.setDataLoader(new QuestionDataLoaderRealtime(context));
        ItemFactory.setDataLoader(new ItemDataLoaderRealtime(context));
        quizGame = new QuizGame();
    }

    public void addQuizListener(QuizListener quizListener) {
        quizGame.addListener(quizListener);
    }

    public void broadcastStartGame() {
        if (packetHandler != null) {
            packetHandler.broadcastGameStarted();
        } else {
            Log.i(TAG, "broadcastStartGame: Attempt to call broadcastGameStarted with " +
                    "null packetHandler, skipping call");
        }
    }

    public void broadcastShowNewView(Class<?> newViewClass) { //todo Figure out next view some other way, a more general way
        String newViewClassString = newViewClass.getSimpleName();
        Log.i(TAG, String.format("broadcastShowNewView: called. newViewClass: '%s'", newViewClassString));

        if (newViewClassString.equals(QuestionView.class.getSimpleName())) {
            if (packetHandler != null) {
                packetHandler.broadcastShowQuestion(quizGame.getNextQuestionCategoryId(), quizGame.getNextQuestionId());
            } else {
                Log.i(TAG, "broadcastShowNewView: attempt to call broadcastShowQuestion on null packetHandler, skipping call");
            }
        } else if (newViewClassString.equals(AfterQuestionScorePageView.class.getSimpleName())) {
            if (packetHandler != null) {
                packetHandler.broadcastShowRoundStats();
            } else {
                Log.i(TAG, "broadcastShowNewView: attempt to call broadcastShowRoundStats on null packetHandler, skipping call");
            }
        } else if (newViewClassString.equals(CategoryView.class.getSimpleName())) {
            quizGame.generateRandomCategoryArray(4);
            Category[] categories = quizGame.getCategorySelectionArray();
            String[] categoriesArr = new String[categories.length];
            for (int i = 0; i < categories.length; i++) {
                categoriesArr[i] = categories[i].getId();
            }
            if (packetHandler != null) {
                packetHandler.broadcastShowCategorySelection(categoriesArr);
            } else {
                Log.i(TAG, "broadcastShowNewView: attempt to call broadcastShowCategorySelection on null packetHandler, skipping call");
            }
        } else {
            Log.e(TAG, "broadcastShowNewView: newViewClass.getSimpleName() does not " +
                    "equal to any case, has it not been implemented yet?, skipping call");
        }
    }

    public void broadcastCategoryVoteResult(Category category) {
        Log.i(TAG, String.format("broadcastCategoryVoteResult: called. categoryId: '%s'", category.getId()));

        quizGame.setCurrentCategory(category);
        if (packetHandler != null) {
            packetHandler.broadcastCategoryVoteResult(category.getId());
        } else {
            Log.i(TAG, "broadcastShowNewView: attempt to call broadcastShowCategorySelection on null packetHandler, skipping call");
        }
    }

    public void startGame() {
        quizGame.resetPlayerReady();
        quizGame.startGame();
        quizGame.startRound();
    }

    public void nextQuestion() {
        quizGame.nextQuestion();
    }

    public void sendAnswer(String givenAnswer, Question question, long timeLeft) {
        Player player = quizGame.getCurrentPlayer();
        if (player != null) { //Hotswap
            quizGame.enterAnswer(player, givenAnswer, question, timeLeft);
            //todo add move to next player if hotswap
        } else { //Network
            if (networkManager != null) {
                Category category = question.getCategory();
                String categoryId = category.getId();
                String questionId = Integer.toString(quizGame.getQuestionIndex(category, question));
                String strTimeLeft = Long.toString(timeLeft);

                if (networkManager.isHost()) { //Enables solo play, as host doesn't know its own id in that case
                    player = quizGame.getPlayer(quizGame.getHostPlayerId());
                    quizGame.enterAnswer(player, givenAnswer, question, timeLeft);
                    BUS.post(new PlayerAnsweredQuestionEvent(player, givenAnswer));

                    if (packetHandler != null) {
                        packetHandler.broadcastPlayerAnsweredQuestion(player.getId(), categoryId, questionId, givenAnswer, strTimeLeft);
                    } else {
                        Log.i(TAG, "sendAnswer: Attempt to call broadcastPlayerAnsweredQuestion with null packetHandler, skipping call");
                    }
                } else {
                    packetHandler.sendRequestAnswerQuestion(categoryId, questionId, givenAnswer, strTimeLeft);
                }

            } else {
                Log.i(TAG, "sendAnswer: Attempt to call getPlayerId with null networkManager, skipping call");
            }
        }
    }

    public void sendCategoryVote(String categoryId) {
        if (packetHandler != null) {
            packetHandler.sendRequestCategoryVote(categoryId);
        } else {
            Log.i(TAG, "sendCategoryVote: Attempt to call sendRequestCategoryVote with null packetHandler, skipping call");
        }
    }

    public List<Player> getPlayers() {
        return quizGame.getPlayers();
    }

    public String getRoomName() {
        if (networkManager != null) {
            return networkManager.getPlayerName();
        } else {
            Log.i(TAG, "getRoomName: Attempt to call getPlayerName with null networkManager, skipping call");
            return null;
        }
    }

    public void setRoomName(String newRoomName) {
        if (networkManager != null) {
            Log.i(TAG, String.format("setRoomName: setting roomName to: '%s'", newRoomName));
            networkManager.setPlayerName(newRoomName);
        } else {
            Log.i(TAG, String.format("setRoomName: Attempt to call setPlayerName with null" +
                    " networkManager. Tried renaming it to: '%s', skipping call", newRoomName));
        }
    }

    public boolean isMe(String playerId) {
        if (isHost()) {
            return quizGame.getHostPlayerId().equals(playerId);
        } else {
            return getClientPlayerId().equals(playerId);
        }
    }

    public boolean isHost() {
        if (networkManager != null) {
            return networkManager.isHost();
        } else {
            return false;
        }
//        return getHostPlayerId().equals(getClientPlayerId());
    }

    public boolean isHotSwap() {
        return quizGame.isHotSwap();
    }

    public String getHostPlayerId() {
        return quizGame.getHostPlayerId();
    }

    public Player getHostPlayer() {
        return quizGame.getPlayer(getHostPlayerId());
    }

    public void setMyReadyStatus(boolean isReady) {
        if (isHost()) {
            setPlayerReady(getHostPlayer(), isReady);
            if (packetHandler != null) {
                packetHandler.broadcastPlayerReadyChange(getHostPlayerId(), isReady);
            } else {
                Log.i(TAG, "setMyReadyStatus: Attempt to call broadcastPlayerReadyChange with null packetHandler, skipping call");
            }
        } else {
            requestSetReady(isReady);
        }
    }

    public void generateRandomCategoryArray(int size) {
        quizGame.generateRandomCategoryArray(size);
    }

    public Category[] getCategorySelectionArray() {
        return quizGame.getCategorySelectionArray();
    }

    private void setPlayerReady(Player player, boolean isReady) {
        if (player != null) {
            player.setReady(isReady);
            fireTeamChangeEvent();
            if (quizGame.checkAllPlayersReady()) {
                Log.i(TAG, "setPlayerReady: All players are ready, triggering AllPlayersReadyEvent.");
                BUS.post(new AllPlayersReadyEvent());
            }
        } else {
            Log.i(TAG, "setPlayerReady: Attempt to call setReady with null player, returning null");
        }
    }

    public String getHostPlayerName() {
        Player hostPlayer = quizGame.getPlayer(quizGame.getHostPlayerId());
        if (hostPlayer != null) {
            return hostPlayer.getName();
        } else {
            Log.i(TAG, "getHostPlayerName: Attempt to call getName with null hostPlayer, returning null");
            return null;
        }
    }

    public void setHostPlayerName(String newPlayerName) {
        Player hostPlayer = quizGame.getPlayer(quizGame.getHostPlayerId());

        if (hostPlayer != null) {
            Log.i(TAG, String.format("setHostPlayerName: setting playerName to: '%s'", newPlayerName));
            hostPlayer.setName(newPlayerName);
        } else {
            Log.i(TAG, String.format("setHostPlayerName: Attempt to call setName with null" +
                    " hostPlayer. Tried renaming it to: '%s', skipping call", newPlayerName));
        }
    }

    public String getClientPlayerId() {
        if (networkManager != null) {
            return networkManager.getPlayerId();
        } else {
            Log.i(TAG, "getClientPlayerId: Attempt to call getPlayerId with null networkManager, returning null");
            return null;
        }
    }

    public String getClientPlayerName() {
        if (networkManager != null) {
            return networkManager.getPlayerName();
        } else {
            Log.i(TAG, "getClientPlayerName: Attempt to call getPlayerName with null networkManager, returning null");
            return null;
        }
    }

    public void setClientPlayerName(String newPlayerName) {
        if (networkManager != null) {
            Log.i(TAG, String.format("setClientPlayerName: setting playerName to: '%s'", newPlayerName));
            networkManager.setPlayerName(newPlayerName);
        } else {
            Log.i(TAG, String.format("setClientPlayerName: Attempt to call setPlayerName with " +
                    "null networkManager. Tried renaming it to: '%s', " +
                    "skipping call", newPlayerName));
        }
    }

    public boolean isRoundOver() {
        return quizGame.isRoundOver();
    }

    public Category getCurrentCategory() {
        return quizGame.getCurrentCategory();
    }

    public void setCurrentCategory(Category currentCategory) {
        quizGame.setCurrentCategory(currentCategory);
    }

    public void resetPlayerData() {
        Log.i(TAG, "resetPlayerData: called.");
        quizGame.resetPlayerData();
    }

    public void resetPlayersReady() {
        Log.i(TAG, "resetPlayerReady: called.");
        quizGame.resetPlayerReady();
    }

    public void joinRoom(Connection roomConnection) {
        if (networkManager != null) {
            networkManager.connectToHost(roomConnection);
        } else {
            Log.i(TAG, "joinRoom: Attempt to call connectToHost with null networkManager, skipping call");
        }
    }

    private void handleLobbySyncProcedure(String senderId) {
        List<Player> playerList = quizGame.getPlayers();

        packetHandler.broadcastLobbySyncStartPacket(senderId, networkManager.getPlayerName(), "epic"); //todo lobbySync replace with actual values
        for (Player player : playerList) {
            //Sync players
            if (!player.getId().equals(senderId)) { //The joining player has already been notified of their
                packetHandler.broadcastPlayerJoined(player.getId(), player.getName());
            }

            //Sync player team
            packetHandler.broadcastPlayerChangeTeam(player.getId(), quizGame.getPlayerTeam(player.getId()).getId());

            //Sync player ready status
            if (player.isReady()) {
                packetHandler.broadcastPlayerReadyChange(player.getId(), player.isReady());
            }
        }

        packetHandler.broadcastLobbySyncEndPacket();
    }

    private void clearNetInCommunicationQueue() {
        if (networkManager != null) {
            networkManager.clearPayloadQueue();
        } else {
            Log.i(TAG, "clearNetInCommunicationQueue: Attempt to call clearPayloadQueue with null networkManager, skipping call");

        }
    }

    private void pauseNetInCommunication() {
        if (networkManager != null) {
            networkManager.setQueueIncomingPayloads(true);
        } else {
            Log.i(TAG, "pauseNetInCommunication: Attempt to call setQueueIncomingPayloads with null networkManager, skipping call");

        }
    }

    public void restoreNetInCommunications() {
        if (networkManager != null) {
            networkManager.processPayloadQueue();
        } else {
            Log.i(TAG, "restoreNetInCommunications: Attempt to call processPayloadQueue with null networkManager, skipping call");

        }
    }

    public void startHostBeacon() {
        if (networkManager != null) {
            appLifecycleHandler.setActive(true);
            networkManager.startHostBeacon();
        } else {
            Log.i(TAG, "startHostBeacon: Attempt to call startHostBeacon with null networkManager, skipping call");
        }
    }

    public void stopHostBeacon() {
        if (networkManager != null) {
            networkManager.stopHostBeacon();
        } else {
            Log.i(TAG, "stopHostBeacon: Attempt to call stopHostBeacon with null networkManager, skipping call");
        }
    }

    public void startScan() {
        if (networkManager != null) {
            appLifecycleHandler.setActive(true);
            networkManager.startScan();
        } else {
            Log.i(TAG, "startScan: Attempt to call startScan with null networkManager, skipping call");
        }
    }

    public void stopScan() {
        if (networkManager != null) {
            networkManager.stopScan();
        } else {
            Log.i(TAG, "stopScan: Attempt to call stopScan with null networkManager, skipping call");
        }
    }

    public void clearConnections() {
        if (networkManager != null) {
            networkManager.stopAllConnections();
        } else {
            Log.i(TAG, "clearConnections: Attempt to call stopAllConnections with null networkManager, skipping call");
        }
    }

    public void resetApp() {
        Log.i(TAG, "resetApp: performing a reset of networkManager and player data");
        quizGame.resetPlayerData();
        quizGame.endGame();
        appLifecycleHandler.setActive(false);

        if (networkManager != null) {
            networkManager.cleanStop();
            networkManager = null;
            packetHandler = null;
        } else {
            Log.i(TAG, "resetApp: Attempt to call cleanStop with null networkManager, skipping call");
        }
    }

    public void addNewPlayer() {
        quizGame.createNewPlayer();
    }

    public void addNewPlayerToEmptyTeam() {
        quizGame.addNewPlayerToEmptyTeam();
    }

    public void createNewHostPlayer() {
        BUS.post(new MyPlayerIdChangedEvent(quizGame.getHostPlayerId()));
        addNewPlayerToTeam("Im the host", quizGame.getHostPlayerId(), true, "1");
    }

    public void addNewPlayerToTeam(String playerName, String playerId, boolean readyStatus, String teamId) {
        quizGame.addNewPlayerToTeam(playerName, playerId, readyStatus, teamId);
    }

    public void fireTeamChangeEvent() {
        quizGame.fireTeamChangeEvent();
    }

    public void fireRoomChangeEvent() {
        //TODO: see comments below
        //Get all connections
        Connection[] roomsArr = networkManager.getConnections();
        List<Connection> rooms = new ArrayList<>(Arrays.asList(roomsArr));

        //Remove all which are not considered rooms
        for (int i = rooms.size() - 1; i >= 0; i--) {
            if (!rooms.get(i).getType().equals(ConnectionType.SERVER)) {
                Log.i(TAG, String.format("fireRoomChangeEvent: excluded connection due to not " +
                        "being considered a room. id:'%s', name:'%s', type:'%s', status:'%s'", rooms.get(i).getId(), rooms.get(i).getName(), rooms.get(i).getType(), rooms.get(i).getState()));
                rooms.remove(i);
            }
        }

        //Post event containing all rooms
        BUS.post(new RoomChangeEvent(rooms));
    }

    public void removePlayer(Player player) {
        quizGame.removePlayer(player);
        if (networkManager != null) { //todo let quizGame trigger a eventbus, and upon this confirmation call disconnect
            networkManager.disconnect(player.getId());
        } else {
            Log.i(TAG, "removePlayer: Attempt to call disconnect with null networkManager, skipping call");
        }
    }

    public void changeMyTeam(String newTeamId, boolean isHost) {
        if (isHost) {
            String myPlayerId = quizGame.getHostPlayerId();
            quizGame.changeTeam(quizGame.getPlayer(myPlayerId), newTeamId);
            if (packetHandler != null) {
                packetHandler.broadcastPlayerChangeTeam(myPlayerId, newTeamId);
            } else {
                Log.i(TAG, "changeMyTeam: Attempt to call broadcastPlayerChangeTeam with null packetHandler, skipping call");
            }
        } else {
            if (packetHandler != null) {
                packetHandler.sendRequestTeamChange(newTeamId);
            } else {
                Log.i(TAG, "changeMyTeam: Attempt to call sendRequestTeamChange with null packetHandler, skipping call");
            }
        }
    }

    private void requestSetReady(boolean isReady) {
        if (packetHandler != null) {
            packetHandler.sendRequestReadyStatus(isReady);
        } else {
            Log.i(TAG, "requestSetReady: Attempt to call sendRequestReadyStatus with null packetHandler, skipping call");
        }
    }

    public Connection getConnection(String playerId) {
        if (networkManager != null) {
            return networkManager.getConnection(playerId);
        }
        return null;
    }

    public void changeTeam(Player player, String newTeamId) {
        quizGame.changeTeam(player, newTeamId);
    }

    public Map<Player, Item> getDrawResult() {
        return quizGame.getWinnings();
    }

    public List<Item> getAllItems() {
        return quizGame.getAllItems();
    }

    /**
     * A method that returns the items in store.
     *
     * @return : A list of items.
     */
    public List<Item> getStoreItems() {
        return quizGame.getStore().getStoreItems();
    }

    /**
     * A method that returns the players score.
     *
     * @return : int value of player's score.
     */
    //TODO player most be removed from this class
    public int getPlayerScore() {
        return p.getScore();
    }

    /**
     * A method that returns if the player is able to buy an item.
     *
     * @param i: the index of the item the player wishes to buy.
     * @return : boolean which indicates if a player is able to buy the item.
     */
    //TODO player most be removed from this class
    public boolean isItemBuyable(int i) {
        return quizGame.getStore().isItemBuyable(i, p);
    }

    /**
     * A method that let's the player buy an item.
     *
     * @param i: the index of the item the player wishes to buy.
     */
    //TODO player most be removed from this class
    public void buy(int i) {
        quizGame.getStore().buy(i, p);
    }

    /**
     * A method that returns the name of an item.
     *
     * @param i: the index of the item.
     * @return : A string with the name of the item.
     */
    public String getItemName(int i) {
        return getStoreItems().get(i).getName();
    }

    /**
     * A method that returns the price of an item.
     *
     * @param i: the index of the item.
     * @return : int with the price of the item.
     */
    public int getItemPrice(int i) {
        return getStoreItems().get(i).getPrice();
    }

    /**
     * A method that returns if an item is bought or available to buy
     *
     * @param i: The index of the item.
     * @return : boolean which indicates if the item is bought or not.
     */
    public boolean isItemBought(int i) {
        return quizGame.getStore().isItemBought(i);
    }

    public String getCurrentPlayerName() {
        if (isHost()) {
            return getHostPlayerName();
        } else if (!isHost()) {
            return getClientPlayerName();
        } else {
            return quizGame.getCurrentPlayer().getName();
        }
    }

    public void incrementCurrentPlayer() {
        quizGame.incrementCurrentPlayer();

    }
}
