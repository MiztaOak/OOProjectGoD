package com.god.kahit;

import android.content.Context;
import android.util.Log;

import com.god.kahit.Events.GameJoinedLobbyEvent;
import com.god.kahit.Events.GameLostConnectionEvent;
import com.god.kahit.Events.MyPlayerIdChangedEvent;
import com.god.kahit.Events.RoomChangeEvent;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;

import static com.god.kahit.model.QuizGame.BUS;

public class Repository { //todo implement a strategy pattern, as we got two different states, host & non-host
    private static final String TAG = Repository.class.getSimpleName();
    private static Repository instance;
    private QuizGame quizGame;
    private NetworkManager networkManager;
    private PacketHandler packetHandler;

    private Repository() {
    }

    public static Repository getInstance() {
        if (instance == null) {
            instance = new Repository();
        }
        return instance;
    }

    public void setupNetwork(Context context, final boolean isHost) {
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
                    pauseNetInCommunication(); //Pause in-coming communication, letting client set up needed logic beforehand
                    packetHandler.sendPlayerId(connection, connection.getId()); //Let host let their id
                    BUS.post(new GameJoinedLobbyEvent());
                }
            }

            @Override
            public void onConnectionLost(@NonNull String id) {
                if (isHost && !quizGame.hasGameStarted()) {
                    //Remove player completely only if game is in lobby
                    Player player = quizGame.getPlayer(id);
                    quizGame.removePlayer(player);
                    packetHandler.broadcastPlayerLeft(id);
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
                    quizGame.getPlayer(quizGame.getHostPlayerId()).setId(playerId);
                    quizGame.setHostPlayerId(playerId);

                    packetHandler.broadcastLobbySyncStartPacket(senderId, "Loot Mansion", "epic"); //todo lobbySync replace with actual values

                    //todo send all sync messages
                    packetHandler.broadcastPlayerJoined(quizGame.getHostPlayerId(), quizGame.getPlayer(quizGame.getHostPlayerId()).getName());
                    packetHandler.broadcastPlayerChangeTeam(quizGame.getHostPlayerId(), quizGame.getPlayerTeam(quizGame.getHostPlayerId()).getId());
                    packetHandler.broadcastLobbyReadyChange(quizGame.getHostPlayerId(), quizGame.getPlayer(quizGame.getHostPlayerId()).isPlayerReady());

                    packetHandler.broadcastLobbySyncEndPacket();

                    BUS.post(new MyPlayerIdChangedEvent(playerId));
                }

                @Override
                public void onPlayerNameChangeRequest(@NonNull String targetPlayerId, @NonNull String newName) {
                    Log.i(TAG, String.format("onPlayerNameChangeRequest: event triggered. callback from: '%s', new name: '%s'", targetPlayerId, newName));
                    packetHandler.broadcastPlayerNameChange(targetPlayerId, newName); //todo only pass to quizgame, let it trigger a broadcast
                }

                @Override
                public void onLobbyReadyChangeRequest(@NonNull String targetPlayerId, @NonNull boolean newState) {
                    Log.i(TAG, String.format("onPlayerReadyStateChangeRequest: event triggered. callback from: '%s', new state: '%s'", targetPlayerId, String.valueOf(newState)));
                    Player targetPlayer = quizGame.getPlayer(targetPlayerId);
                    if (targetPlayer != null) {
                        targetPlayer.setPlayerReady(newState);
                        fireTeamChangeEvent();
                        packetHandler.broadcastLobbyReadyChange(targetPlayerId, newState);  //todo only pass to quizGame, let it trigger a broadcast
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
                public void onLobbyReadyChangeEvent(@NonNull String targetId, @NonNull boolean newState) {
                    Log.i(TAG, String.format("onLobbyReadyChangeEvent: event triggered. targetId: '%s', new state: '%s'", targetId, newState));
                    Player player = quizGame.getPlayer(targetId);
                    if (player != null) {
                        player.setPlayerReady(newState);
                        fireTeamChangeEvent();
                    } else {
                        Log.i(TAG, String.format("onLobbyReadyChangeEvent: Target player (id: '%s') does not exist in quizGame! Skipping ready change event", targetId));
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
                    //todo implement onGameStartedEvent
                }

                @Override
                public void onLobbySyncStartEvent(@NonNull String targetPlayerId, @NonNull String roomName, @NonNull String gameModeId) {
                    Log.i(TAG, "onLobbySyncStartEvent: event triggered.");
                    //todo implement onLobbySyncStartEvent
                }

                @Override
                public void onLobbySyncEndEvent() {
                    Log.i(TAG, "onLobbySyncEndEvent: event triggered.");
                    //todo implement onLobbySyncEndEvent
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

    public void startGame() {
        quizGame.startGame();
        quizGame.startRound();
    }

    public void nextQuestion() {
        quizGame.nextQuestion();
    }

    public void sendAnswer(String givenAnswer, Question question, long timeLeft) {
        quizGame.enterAnswer(givenAnswer, question, timeLeft);
    }

    public List<Player> getPlayers() {
        return quizGame.getPlayers();
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
        quizGame.resetPlayerData();
    }

    public void joinRoom(Connection roomConnection) {
        if (networkManager != null) {
            networkManager.connectToHost(roomConnection);
        } else {
            Log.i(TAG, "joinRoom: Attempt to call connectToHost with null networkManager, skipping call");
        }
    }

    public void pauseNetInCommunication() {
        networkManager.setQueueIncomingPayloads(true);
    }

    public void restoreNetInCommunications() {
        networkManager.processPayloadQueue();
    }

    public void startHostBeacon() {
        if (networkManager != null) {
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

    public void requestChangeMyTeam(String newTeamId, boolean isHost) {
        if (isHost) {
            String myPlayerId = quizGame.getHostPlayerId();
            quizGame.changeTeam(quizGame.getPlayer(myPlayerId), newTeamId);
            if (packetHandler != null) {
                packetHandler.broadcastPlayerChangeTeam(myPlayerId, newTeamId);
            } else {
                Log.i(TAG, "requestChangeMyTeam: Attempt to call broadcastPlayerChangeTeam with null packetHandler, skipping call");
            }
        } else {
            if (packetHandler != null) {
                packetHandler.sendRequestTeamChange(newTeamId);
            } else {
                Log.i(TAG, "requestChangeMyTeam: Attempt to call sendRequestTeamChange with null packetHandler, skipping call");
            }
        }
    }

    public void requestSetReady(boolean isReady) {
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

    public void changeTeam(Player player, int newTeamId) {
        quizGame.changeTeam(player, newTeamId);
    }

    public Map<Player, Item> getDrawResult() {
        return quizGame.getWinnings();
    }

    public List<Item> getAllItems() {
        return quizGame.getAllItems();
    }

}
