package com.god.kahit.networkManager;

import android.util.Log;

import com.god.kahit.networkManager.callbacks.ClientRequestsCallback;
import com.god.kahit.networkManager.callbacks.HostEventCallback;
import com.god.kahit.networkManager.packets.EventCategoryPlayerVotePacket;
import com.god.kahit.networkManager.packets.EventCategoryVoteResultPacket;
import com.god.kahit.networkManager.packets.EventGameStartedPacket;
import com.god.kahit.networkManager.packets.EventLobbySyncEndPacket;
import com.god.kahit.networkManager.packets.EventLobbySyncStartPacket;
import com.god.kahit.networkManager.packets.EventPlayerAnsweredQuestionPacket;
import com.god.kahit.networkManager.packets.EventPlayerChangeTeamPacket;
import com.god.kahit.networkManager.packets.EventPlayerJoinedPacket;
import com.god.kahit.networkManager.packets.EventPlayerLeftPacket;
import com.god.kahit.networkManager.packets.EventPlayerNameChangePacket;
import com.god.kahit.networkManager.packets.EventPlayerReadyChangePacket;
import com.god.kahit.networkManager.packets.EventShowCategorySelectionPacket;
import com.god.kahit.networkManager.packets.EventShowGameResultsPacket;
import com.god.kahit.networkManager.packets.EventShowLotteryPacket;
import com.god.kahit.networkManager.packets.EventShowQuestionPacket;
import com.god.kahit.networkManager.packets.EventShowRoundStatsPacket;
import com.god.kahit.networkManager.packets.EventTeamCreatedPacket;
import com.god.kahit.networkManager.packets.EventTeamDeletedPacket;
import com.god.kahit.networkManager.packets.EventTeamNameChangePacket;
import com.god.kahit.networkManager.packets.Packet;
import com.god.kahit.networkManager.packets.PlayerIdPacket;
import com.god.kahit.networkManager.packets.RequestCategoryPlayerVotePacket;
import com.god.kahit.networkManager.packets.RequestPlayerAnswerQuestionPacket;
import com.god.kahit.networkManager.packets.RequestPlayerChangeTeamPacket;
import com.god.kahit.networkManager.packets.RequestPlayerNameChangePacket;
import com.god.kahit.networkManager.packets.RequestPlayerReadyChangePacket;
import com.god.kahit.networkManager.packets.RequestTeamNameChangePacket;

import java.util.Arrays;

/**
 * responsibility: This class is responsible for the construction, deconstruction and sending of
 * network packets. It also provides callbacks for all types of packets.
 * used-by: This class is used in the following classes:
 * Repository
 * @author: Mats Cedervall
 */
public class PacketHandler {
    private static final String TAG = "PacketHandler";

    private NetworkManager networkManager;

    private HostEventCallback hostEventCallback;
    private ClientRequestsCallback clientRequestsCallback;

    public PacketHandler(NetworkManager networkManager, HostEventCallback hostEventCallback) {
        this.networkManager = networkManager;
        this.hostEventCallback = hostEventCallback;
    }

    public PacketHandler(NetworkManager networkManager, ClientRequestsCallback clientRequestsCallback) {
        this.networkManager = networkManager;
        this.clientRequestsCallback = clientRequestsCallback;
    }

    /***
     * Method used to handle an incoming byte[] payload
     * @param id connection id of sender
     * @param payload received byte[] payload
     */
    public void handleReceivedPayload(String id, byte[] payload) {
        int receivedPacketID = Integer.valueOf(Byte.toString(payload[0]));

        switch (receivedPacketID) {
            case (PlayerIdPacket.PACKET_ID): //0
                handlePlayerIdPacket(id, payload);
                break;

            case (EventLobbySyncStartPacket.PACKET_ID): //1
                handleLobbySyncStartPacket(payload);
                break;

            case (EventLobbySyncEndPacket.PACKET_ID): //2
                handleLobbySyncEndPacket();
                break;

            case (RequestPlayerNameChangePacket.PACKET_ID): //3
                handleRequestPlayerNameChangePacket(id, payload);
                break;

            case (EventPlayerNameChangePacket.PACKET_ID): //4
                handleEventPlayerNameChangePacket(payload);
                break;

            case (RequestPlayerReadyChangePacket.PACKET_ID): //5
                handleRequestPlayerReadyChangePacket(id, payload);
                break;

            case (EventPlayerReadyChangePacket.PACKET_ID): //6
                handleEventPlayerReadyChangePacket(payload);
                break;

            case (RequestTeamNameChangePacket.PACKET_ID): //7
                handleRequestTeamNameChangePacket(id, payload);
                break;

            case (EventTeamNameChangePacket.PACKET_ID): //8
                handleEventTeamNameChangePacket(payload);
                break;

            case (EventPlayerJoinedPacket.PACKET_ID): //9
                handleEventPlayerJoinedPacket(payload);
                break;

            case (EventPlayerLeftPacket.PACKET_ID): //10
                handleEventPlayerLeftPacket(payload);
                break;

            case (RequestPlayerChangeTeamPacket.PACKET_ID): //11
                handleRequestPlayerChangeTeamPacket(id, payload);
                break;

            case (EventPlayerChangeTeamPacket.PACKET_ID): //12
                handleEventPlayerChangeTeamPacket(payload);
                break;

            case (EventTeamCreatedPacket.PACKET_ID): //13
                handleEventTeamCreatedPacket(payload);
                break;

            case (EventTeamDeletedPacket.PACKET_ID): //14
                handleEventTeamDeletedPacket(payload);
                break;

            case (EventGameStartedPacket.PACKET_ID): //15
                handleEventGameStartedPacket();
                break;

            case (EventShowQuestionPacket.PACKET_ID): //16
                handleEventShowQuestionPacket(payload);
                break;

            case (EventShowRoundStatsPacket.PACKET_ID): //17
                handleEventShowRoundStatsPacket();
                break;

            case (EventShowCategorySelectionPacket.PACKET_ID): //18
                handleEventShowCategorySelectionPacket(payload);
                break;

            case (EventShowLotteryPacket.PACKET_ID): //19
                handleEventShowLotteryPacket(payload);
                break;

            case (EventShowGameResultsPacket.PACKET_ID): //20
                handleEventShowGameResultsPacket();
                break;

            case (EventCategoryPlayerVotePacket.PACKET_ID): //21
                handleEventCategoryPlayerVotePacket(payload);
                break;

            case (RequestCategoryPlayerVotePacket.PACKET_ID): //22
                handleRequestCategoryPlayerVotePacket(id, payload);
                break;

            case (EventCategoryVoteResultPacket.PACKET_ID): //23
                handleEventCategoryVoteResultPacket(payload);
                break;

            case (EventPlayerAnsweredQuestionPacket.PACKET_ID): //24
                handleEventPlayerAnsweredQuestionPacket(payload);
                break;

            case (RequestPlayerAnswerQuestionPacket.PACKET_ID): //25
                handleRequestPlayerAnswerQuestionPacket(id, payload);
                break;

            default:
                Log.i(TAG, String.format("handleReceivedPayload: Unknown packetID from: '%s', " +
                        "packetId:'%s'", id, receivedPacketID));
        }
    }

    // ====================== Handle methods ======================

    private void handlePlayerIdPacket(String senderId, byte[] payload) {
        String playerId = PlayerIdPacket.getPlayerId(payload);
        Log.i(TAG, String.format("handlePlayerIdPacket: Received new playerId: '%s'", playerId));
        networkManager.setMyConnectionId(playerId);

        if (clientRequestsCallback != null) {
            clientRequestsCallback.onReceivedMyConnectionId(senderId, playerId);
        }

        if (hostEventCallback != null) {
            hostEventCallback.onReceivedMyConnectionId(playerId);
        }
    }

    private void handleLobbySyncStartPacket(byte[] payload) {
        String targetPlayerId = EventLobbySyncStartPacket.getTargetPlayerId(payload);
        String roomName = EventLobbySyncStartPacket.getRoomName(payload);
        String gameModeId = EventLobbySyncStartPacket.getGameModeId(payload);
        Log.i(TAG, String.format("handleLobbySyncStartPacket: Received " +
                "EventLobbySyncStartPacket. targetPlayerId: '%s', roomName: '%s', " +
                "gameModeId: '%s'", targetPlayerId, roomName, gameModeId));

        if (hostEventCallback != null) {
            hostEventCallback.onLobbySyncStartEvent(targetPlayerId, roomName, gameModeId);
        }
    }

    private void handleLobbySyncEndPacket() {
        Log.i(TAG, "handleLobbySyncEndPacket: Received EventLobbySyncEndPacket.");

        if (hostEventCallback != null) {
            hostEventCallback.onLobbySyncEndEvent();
        }
    }

    private void handleRequestPlayerNameChangePacket(String senderId, byte[] payload) {
        String newName = RequestPlayerNameChangePacket.getNewPlayerName(payload);
        Log.i(TAG, String.format("handleRequestPlayerNameChangePacket: Received player name change request from '%s'. New name is '%s'", senderId, newName));

        if (clientRequestsCallback != null) {
            clientRequestsCallback.onPlayerNameChangeRequest(senderId, newName);
        }
    }

    private void handleEventPlayerNameChangePacket(byte[] payload) {
        String targetPlayerId = EventPlayerNameChangePacket.getTargetPlayerId(payload);
        String newName = EventPlayerNameChangePacket.getNewPlayerName(payload);
        Log.i(TAG, String.format("handleEventPlayerNameChangePacket: Received playerNameChangedEvent. targetPlayerId: '%s', newName: '%s'", targetPlayerId, newName));
        if (hostEventCallback != null) {
            hostEventCallback.onPlayerNameChangeEvent(targetPlayerId, newName);
        }
    }

    private void handleRequestPlayerReadyChangePacket(String senderId, byte[] payload) {
        boolean newState = RequestPlayerReadyChangePacket.getNewState(payload);
        Log.i(TAG, String.format("RequestPlayerReadyChangePacket: Received player ready state change request from '%s'. New state is '%s'", senderId, newState));

        if (clientRequestsCallback != null) {
            clientRequestsCallback.onPlayerReadyChangeRequest(senderId, newState);
        }
    }

    private void handleEventPlayerReadyChangePacket(byte[] payload) {
        String targetPlayerId = EventPlayerReadyChangePacket.getTargetPlayerId(payload);
        boolean newState = EventPlayerReadyChangePacket.getNewState(payload);
        Log.i(TAG, String.format("handleEventPlayerReadyChangePacket: Received EventPlayerReadyChangePacket. targetPlayerId: '%s', new state: '%s'", targetPlayerId, newState));
        if (hostEventCallback != null) {
            hostEventCallback.onPlayerReadyChangeEvent(targetPlayerId, newState);
        }
    }

    private void handleRequestTeamNameChangePacket(String senderId, byte[] payload) {
        String teamId = RequestTeamNameChangePacket.getTeamId(payload);
        String newTeamName = RequestTeamNameChangePacket.getNewTeamName(payload);
        Log.i(TAG, String.format("handleRequestTeamNameChangePacket: Received team name change request from '%s'. TeamId: '%s', new team name is '%s'", senderId, teamId, newTeamName));

        if (clientRequestsCallback != null) {
            clientRequestsCallback.onTeamNameChangeRequest(teamId, newTeamName);
        }
    }

    private void handleEventTeamNameChangePacket(byte[] payload) {
        String teamId = EventTeamNameChangePacket.getTeamId(payload);
        String newTeamName = EventTeamNameChangePacket.getNewTeamName(payload);
        Log.i(TAG, String.format("handleEventTeamNameChangePacket: Received teamNameChangedEvent. teamId: '%s', newTeamName: '%s'", teamId, newTeamName));

        if (hostEventCallback != null) {
            hostEventCallback.onTeamNameChangeEvent(teamId, newTeamName);
        }
    }

    private void handleEventPlayerJoinedPacket(byte[] payload) {
        String playerId = EventPlayerJoinedPacket.getPlayerId(payload);
        String playerName = EventPlayerJoinedPacket.getPlayerName(payload);
        Log.i(TAG, String.format("handleEventPlayerJoinedPacket: Received EventPlayerJoinedPacket. playerId: '%s', playerName: '%s'", playerId, playerName));

        if (hostEventCallback != null) {
            hostEventCallback.onPlayerJoinedEvent(playerId, playerName);
        }
    }

    private void handleEventPlayerLeftPacket(byte[] payload) {
        String playerId = EventPlayerLeftPacket.getPlayerId(payload);
        Log.i(TAG, String.format("handleEventPlayerLeftPacket: Received EventPlayerLeftPacket. playerId: '%s'", playerId));

        if (hostEventCallback != null) {
            hostEventCallback.onPlayerLeftEvent(playerId);
        }
    }

    private void handleRequestPlayerChangeTeamPacket(String senderId, byte[] payload) {
        String newTeamId = RequestPlayerChangeTeamPacket.getNewTeamId(payload);
        Log.i(TAG, String.format("handleRequestPlayerChangeTeamPacket: Received player team change request from '%s'. newTeamId: '%s'", senderId, newTeamId));

        if (clientRequestsCallback != null) {
            clientRequestsCallback.onPlayerTeamChangeRequest(senderId, newTeamId);
        }
    }

    private void handleEventPlayerChangeTeamPacket(byte[] payload) {
        String targetPlayerId = EventPlayerChangeTeamPacket.getTargetPlayerId(payload);
        String newTeamId = EventPlayerChangeTeamPacket.getNewTeamId(payload);
        Log.i(TAG, String.format("handleEventPlayerChangeTeamPacket: Received EventPlayerChangeTeamPacket. targetPlayerId: %s, newTeamId: '%s'", targetPlayerId, newTeamId));

        if (hostEventCallback != null) {
            hostEventCallback.onPlayerChangeTeamEvent(targetPlayerId, newTeamId);
        }
    }

    private void handleEventTeamCreatedPacket(byte[] payload) {
        String newTeamId = EventTeamCreatedPacket.getNewTeamId(payload);
        String newTeamName = EventTeamCreatedPacket.getNewTeamName(payload);
        Log.i(TAG, String.format("handleEventTeamCreatedPacket: Received EventTeamCreatedPacket. newTeamId: %s, newTeamName: '%s'", newTeamId, newTeamName));

        if (hostEventCallback != null) {
            hostEventCallback.onTeamCreatedEvent(newTeamId, newTeamName);
        }
    }

    private void handleEventTeamDeletedPacket(byte[] payload) {
        String teamId = EventTeamDeletedPacket.getTeamId(payload);
        Log.i(TAG, String.format("handleEventTeamDeletedPacket: Received EventTeamDeletedPacket. teamId: %s", teamId));

        if (hostEventCallback != null) {
            hostEventCallback.onTeamDeletedEvent(teamId);
        }
    }

    private void handleEventGameStartedPacket() {
        Log.i(TAG, "handleEventGameStartedPacket: Received EventGameStartedPacket");

        if (hostEventCallback != null) {
            hostEventCallback.onGameStartedEvent();
        }
    }

    private void handleEventShowQuestionPacket(byte[] payload) {
        String categoryId = EventShowQuestionPacket.getCategoryId(payload);
        String questionId = EventShowQuestionPacket.getQuestionId(payload);
        Log.i(TAG, String.format("handleEventShowQuestionPacket: Received EventShowQuestionPacket. categoryId: '%s', questionId: %s", categoryId, questionId));

        if (hostEventCallback != null) {
            hostEventCallback.onShowQuestionEvent(categoryId, questionId);
        }
    }

    private void handleEventShowRoundStatsPacket() {
        Log.i(TAG, "handleEventShowRoundStatsPacket: Received EventShowRoundStatsPacket");

        if (hostEventCallback != null) {
            hostEventCallback.onShowRoundStatsEvent();
        }
    }

    private void handleEventShowCategorySelectionPacket(byte[] payload) {
        String[] categoryIds = EventShowCategorySelectionPacket.getCategoryIds(payload);
        Log.i(TAG, String.format("handleEventShowCategorySelectionPacket: Received EventShowCategorySelectionPacket. categoryIds: %s", Arrays.toString(categoryIds)));

        if (hostEventCallback != null) {
            hostEventCallback.onShowCategorySelectionEvent(categoryIds);
        }
    }

    private void handleEventShowLotteryPacket(byte[] payload) {
        String[][] playersWonItemsMatrix = EventShowLotteryPacket.getPlayersWonItemsMatrix(payload);
        Log.i(TAG, String.format("handleEventShowLotteryPacket: Received EventShowLotteryPacket. playersWonItemsMatrix: %s", Arrays.toString(playersWonItemsMatrix)));

        if (hostEventCallback != null) {
            hostEventCallback.onShowLotteryEvent(playersWonItemsMatrix);
        }
    }

    private void handleEventShowGameResultsPacket() {
        Log.i(TAG, "handleEventShowGameResultsPacket: Received EventShowGameResultsPacket");

        if (hostEventCallback != null) {
            hostEventCallback.onShowGameResultsEvent();
        }
    }

    private void handleEventCategoryPlayerVotePacket(byte[] payload) {
        String targetPlayerId = EventCategoryPlayerVotePacket.getTargetPlayerId(payload);
        String categoryId = EventCategoryPlayerVotePacket.getCategoryId(payload);
        Log.i(TAG, String.format("handleEventCategoryPlayerVotePacket: Received EventCategoryPlayerVotePacket. targetPlayerId: %s, categoryId: '%s'", targetPlayerId, categoryId));

        if (hostEventCallback != null) {
            hostEventCallback.onCategoryPlayerVoteEvent(targetPlayerId, categoryId);
        }
    }

    private void handleRequestCategoryPlayerVotePacket(String senderId, byte[] payload) {
        String categoryId = RequestCategoryPlayerVotePacket.getCategoryId(payload);
        Log.i(TAG, String.format("handleRequestCategoryPlayerVotePacket: Received RequestCategoryPlayerVotePacket from '%s'. categoryId: '%s'", senderId, categoryId));

        if (clientRequestsCallback != null) {
            clientRequestsCallback.onCategoryPlayerVoteRequest(senderId, categoryId);
        }
    }

    private void handleEventCategoryVoteResultPacket(byte[] payload) {
        String categoryId = EventCategoryVoteResultPacket.getCategoryId(payload);
        Log.i(TAG, String.format("handleEventCategoryVoteResultPacket: Received EventCategoryVoteResultPacket. categoryId: '%s'", categoryId));

        if (hostEventCallback != null) {
            hostEventCallback.onCategoryVoteResultEvent(categoryId);
        }
    }

    private void handleEventPlayerAnsweredQuestionPacket(byte[] payload) {
        String targetPlayerId = EventPlayerAnsweredQuestionPacket.getTargetPlayerId(payload);
        String categoryId = EventPlayerAnsweredQuestionPacket.getCategoryId(payload);
        String questionId = EventPlayerAnsweredQuestionPacket.getQuestionId(payload);
        String givenAnswer = EventPlayerAnsweredQuestionPacket.getGivenAnswer(payload);
        String timeLeft = EventPlayerAnsweredQuestionPacket.getTimeLeft(payload);
        Log.i(TAG, String.format("handleEventPlayerAnsweredQuestionPacket: Received " +
                "EventPlayerAnsweredQuestionPacket. targetPlayerId: %s, " +
                "categoryId: '%s', questionId: '%s', givenAnswer: '%s', " +
                "timeLeft: '%s'", targetPlayerId, categoryId, questionId, givenAnswer, timeLeft));

        if (hostEventCallback != null) {
            hostEventCallback.onPlayerAnsweredQuestionEvent(targetPlayerId, categoryId,
                    questionId, givenAnswer, Long.valueOf(timeLeft));
        }
    }

    private void handleRequestPlayerAnswerQuestionPacket(String senderId, byte[] payload) {
        String categoryId = RequestPlayerAnswerQuestionPacket.getCategoryId(payload);
        String questionId = RequestPlayerAnswerQuestionPacket.getQuestionId(payload);
        String givenAnswer = RequestPlayerAnswerQuestionPacket.getGivenAnswer(payload);
        String timeLeft = RequestPlayerAnswerQuestionPacket.getTimeLeft(payload);
        Log.i(TAG, String.format("handleRequestPlayerAnswerQuestionPacket: Received " +
                        "RequestPlayerAnswerQuestionPacket from '%s'. categoryId: '%s', " +
                        "questionId: '%s', givenAnswer: '%s', timeLeft: '%s'", senderId,
                categoryId, questionId, givenAnswer, timeLeft));

        if (clientRequestsCallback != null) {
            clientRequestsCallback.onPlayerAnsweredQuestionRequest(senderId, categoryId, questionId,
                    givenAnswer, Long.valueOf(timeLeft));
        }
    }

    // ====================== Send methods ======================

    /**
     * Method to send a PlayerIdPacket to a given connection
     *
     * @param connection Connection to send the packet to
     * @param playerId   String playerId
     */
    public void sendPlayerId(Connection connection, String playerId) {
        Log.i(TAG, String.format("sendPlayerId: sending PlayerIdPacket to '%s'", playerId));
        Packet packet = new PlayerIdPacket(playerId);
        networkManager.sendBytePayload(connection, packet.getBuiltPacket());
    }

    /**
     * Method to send a RequestPlayerNameChangePacket to host
     *
     * @param newName String newName
     */
    public void sendRequestPlayerNameChange(String newName) {
        Log.i(TAG, String.format("sendRequestPlayerNameChange: sending RequestPlayerNameChangePacket to host: '%s'", networkManager.getConnectionHost().getId()));
        Packet packet = new RequestPlayerNameChangePacket(newName);
        networkManager.sendBytePayload(networkManager.getConnectionHost(), packet.getBuiltPacket());
    }

    /**
     * Method to send a RequestPlayerReadyChangePacket to host
     *
     * @param newState boolean new ready state
     */
    public void sendRequestReadyStatus(boolean newState) {
        Log.i(TAG, String.format("sendRequestReadyStatus: sending RequestPlayerReadyChangePacket to host: '%s'", networkManager.getConnectionHost().getId()));
        Packet packet = new RequestPlayerReadyChangePacket(newState);
        networkManager.sendBytePayload(networkManager.getConnectionHost(), packet.getBuiltPacket());
    }

    /**
     * Method to send a RequestTeamNameChangePacket to host
     *
     * @param teamId      String teamId of affected team
     * @param newTeamName String new team name
     */
    public void sendRequestTeamNameChange(String teamId, String newTeamName) {
        Log.i(TAG, String.format("sendRequestTeamNameChange: sending RequestTeamNameChangePacket to host: '%s'", networkManager.getConnectionHost().getId()));
        Packet packet = new RequestTeamNameChangePacket(teamId, newTeamName);
        networkManager.sendBytePayload(networkManager.getConnectionHost(), packet.getBuiltPacket());
    }

    /**
     * Method to send a RequestPlayerChangeTeamPacket to host
     *
     * @param newTeamID String teamId of the new team
     */
    public void sendRequestTeamChange(String newTeamID) {
        Log.i(TAG, String.format("sendRequestTeamChange: sending RequestPlayerChangeTeamPacket to host: '%s'", networkManager.getConnectionHost().getId()));
        Packet packet = new RequestPlayerChangeTeamPacket(newTeamID);
        networkManager.sendBytePayload(networkManager.getConnectionHost(), packet.getBuiltPacket());
    }

    /**
     * Method to send a RequestPlayerBuyItemPacket to host
     *
     * @param itemID String item id
     */
    public void sendRequestBuyItem(String itemID) { }

    /**
     * Method to send a RequestPlayerAnswerQuestionPacket to host
     *
     * @param categoryId  String categoryId of answered question
     * @param questionId  String questionId of answered question
     * @param givenAnswer String answer text
     * @param timeLeft    String long of time left converted to a string
     */
    public void sendRequestAnswerQuestion(String categoryId, String questionId, String givenAnswer, String timeLeft) {
        Log.i(TAG, String.format("sendRequestAnswerQuestion: sending RequestPlayerAnswerQuestionPacket to host: '%s'", networkManager.getConnectionHost().getId()));
        Packet packet = new RequestPlayerAnswerQuestionPacket(categoryId, questionId, givenAnswer, timeLeft);
        networkManager.sendBytePayload(networkManager.getConnectionHost(), packet.getBuiltPacket());
    }

    /**
     * Method to send a RequestCategoryPlayerVotePacket to host
     *
     * @param categoryId String category id of the voted for category
     */
    public void sendRequestCategoryVote(String categoryId) {
        Log.i(TAG, String.format("sendRequestCategoryVote: sending RequestCategoryPlayerVotePacket to host: '%s'", networkManager.getConnectionHost().getId()));
        Packet packet = new RequestCategoryPlayerVotePacket(categoryId);
        networkManager.sendBytePayload(networkManager.getConnectionHost(), packet.getBuiltPacket());
    }

    // ====================== Broadcast methods ======================

    /**
     * Method used to broadcast a EventLobbySyncStartPacket to all connected endpoints
     *
     * @param targetPlayerId String affected player's id
     * @param lobbyName      String lobby name
     * @param gameModeId     String lobby game mode id
     */
    public void broadcastLobbySyncStartPacket(String targetPlayerId, String lobbyName,
                                              String gameModeId) {
        Log.i(TAG, String.format("broadcastLobbySyncStartPacket: broadcasting " +
                "EventLobbySyncStartPacket. targetPlayerId: '%s', roomName: '%s', " +
                "gameModeId: '%s'", targetPlayerId, lobbyName, gameModeId));

        //Start queuing incoming payloads
        networkManager.setQueueIncomingPayloads(true);

        //Send packet
        Packet packet = new EventLobbySyncStartPacket(targetPlayerId, lobbyName, gameModeId);
        networkManager.broadcastBytePayload(packet.getBuiltPacket());
    }

    /**
     * Method used to broadcast a EventLobbySyncEndPacket to all connected endpoints
     */
    public void broadcastLobbySyncEndPacket() {
        Log.i(TAG, "broadcastLobbySyncEndPacket: broadcasting EventLobbySyncEndPacket.");
        Packet packet = new EventLobbySyncEndPacket();
        networkManager.broadcastBytePayload(packet.getBuiltPacket());

        //As sync is complete, process received payloads and reset setQueueIncomingPayloads
        networkManager.processPayloadQueue();
    }

    /**
     * Method used to broadcast a EventPlayerNameChangePacket to all connected endpoints
     *
     * @param targetPlayerId String affected player's id
     * @param newName        String new player name
     */
    public void broadcastPlayerNameChange(String targetPlayerId, String newName) {
        Log.i(TAG, String.format("broadcastPlayerNameChange: broadcasting" +
                " EventPlayerNameChangePacket. targetPlayerId: '%s', " +
                "newName: '%s'", targetPlayerId, newName));
        Packet packet = new EventPlayerNameChangePacket(targetPlayerId, newName);
        networkManager.broadcastBytePayload(packet.getBuiltPacket());
    }

    /**
     * Method used to broadcast a EventPlayerReadyChangePacket to all connected endpoints
     *
     * @param targetPlayerId String affected player's id
     * @param newState       boolean new player ready state
     */
    public void broadcastPlayerReadyChange(String targetPlayerId, boolean newState) {
        Log.i(TAG, String.format("broadcastPlayerReadyChange: broadcasting EventPlayerReadyChangePacket. targetPlayerId: '%s', newState: '%s'", targetPlayerId, newState));
        Packet packet = new EventPlayerReadyChangePacket(targetPlayerId, newState);
        networkManager.broadcastBytePayload(packet.getBuiltPacket());
    }

    /**
     * Method used to broadcast a EventTeamNameChangePacket to all connected endpoints
     *
     * @param teamId      String affected team's id
     * @param newTeamName String new team name
     */
    public void broadcastTeamNameChange(String teamId, String newTeamName) {
        Log.i(TAG, String.format("broadcastTeamNameChange: broadcasting EventTeamNameChangePacket. teamId: '%s', newTeamName: '%s'", teamId, newTeamName));
        Packet packet = new EventTeamNameChangePacket(teamId, newTeamName);
        networkManager.broadcastBytePayload(packet.getBuiltPacket());
    }

    /**
     * Method used to broadcast a EventPlayerJoinedPacket to all connected endpoints
     *
     * @param playerId   String affected player's id
     * @param playerName String new player name
     */
    public void broadcastPlayerJoined(String playerId, String playerName) {
        Log.i(TAG, String.format("broadcastPlayerJoined: broadcasting EventPlayerJoinedPacket. playerId: '%s', playerName: '%s'", playerId, playerName));
        Packet packet = new EventPlayerJoinedPacket(playerId, playerName);
        networkManager.broadcastBytePayload(packet.getBuiltPacket());
    }

    /**
     * Method used to broadcast a EventPlayerLeftPacket to all connected endpoints
     *
     * @param playerId String affected player's id
     */
    public void broadcastPlayerLeft(String playerId) {
        Log.i(TAG, String.format("broadcastPlayerLeft: broadcasting EventPlayerLeftPacket. playerId: '%s'", playerId));
        Packet packet = new EventPlayerLeftPacket(playerId);
        networkManager.broadcastBytePayload(packet.getBuiltPacket());
    }

    /**
     * Method used to broadcast a EventPlayerChangeTeamPacket to all connected endpoints
     *
     * @param targetPlayerId String affected player's id
     * @param newTeamId      String new team's id
     */
    public void broadcastPlayerChangeTeam(String targetPlayerId, String newTeamId) {
        Log.i(TAG, String.format("broadcastPlayerTeamChange: broadcasting EventPlayerChangeTeamPacket. targetPlayerId: '%s', newTeamId: '%s'", targetPlayerId, newTeamId));
        Packet packet = new EventPlayerChangeTeamPacket(targetPlayerId, newTeamId);
        networkManager.broadcastBytePayload(packet.getBuiltPacket());
    }

    /**
     * Method used to broadcast a EventTeamCreatedPacket to all connected endpoints
     *
     * @param newTeamId   String created team's id
     * @param newTeamName String created team's name
     */
    public void broadcastTeamCreated(String newTeamId, String newTeamName) {
        Log.i(TAG, String.format("broadcastTeamCreated: broadcasting EventTeamCreatedPacket. newTeamId: '%s', newTeamName: '%s'", newTeamId, newTeamName));
        Packet packet = new EventTeamCreatedPacket(newTeamId, newTeamName);
        networkManager.broadcastBytePayload(packet.getBuiltPacket());
    }

    /**
     * Method used to broadcast a EventTeamDeletedPacket to all connected endpoints
     *
     * @param teamId String affected team's id
     */
    public void broadcastTeamDeleted(String teamId) {
        Log.i(TAG, String.format("broadcastTeamDeleted: broadcasting EventTeamDeletedPacket. teamId: '%s'", teamId));
        Packet packet = new EventTeamDeletedPacket(teamId);
        networkManager.broadcastBytePayload(packet.getBuiltPacket());
    }

    /**
     * Method used to broadcast a EventGameStartedPacket to all connected endpoints
     */
    public void broadcastGameStarted() {
        Log.i(TAG, "broadcastGameStarted: broadcasting EventGameStartedPacket.");
        Packet packet = new EventGameStartedPacket();
        networkManager.broadcastBytePayload(packet.getBuiltPacket());
    }

    /**
     * Method used to broadcast a EventShowQuestionPacket to all connected endpoints
     *
     * @param categoryId String category id of the question to be shown
     * @param questionId String question id of the question to be shown
     */
    public void broadcastShowQuestion(String categoryId, String questionId) {
        Log.i(TAG, String.format("broadcastShowQuestion: broadcasting EventShowQuestionPacket. categoryId: '%s', questionId: '%s'", categoryId, questionId));
        Packet packet = new EventShowQuestionPacket(categoryId, questionId);
        networkManager.broadcastBytePayload(packet.getBuiltPacket());
    }

    /**
     * Method used to broadcast a EventShowRoundStatsPacket to all connected endpoints
     */
    public void broadcastShowRoundStats() {
        Log.i(TAG, "broadcastShowRoundStats: broadcasting EventShowRoundStatsPacket.");
        Packet packet = new EventShowRoundStatsPacket();
        networkManager.broadcastBytePayload(packet.getBuiltPacket());
    }

    /**
     * Method used to broadcast a EventShowCategorySelectionPacket to all connected endpoints
     *
     * @param categoryIds String category id of selected category
     */
    public void broadcastShowCategorySelection(String[] categoryIds) {
        Log.i(TAG, String.format("broadcastShowCategorySelection: broadcasting EventShowCategorySelectionPacket. categoryIds: '%s'", Arrays.toString(categoryIds)));
        Packet packet = new EventShowCategorySelectionPacket(categoryIds);
        networkManager.broadcastBytePayload(packet.getBuiltPacket());
    }

    /**
     * Method used to broadcast a EventShowLotteryPacket to all connected endpoints
     *
     * @param playersWonItemsMatrix String[][] containing each player and what item it has won
     */
    public void broadcastShowLottery(String[][] playersWonItemsMatrix) {
        Log.i(TAG, String.format("broadcastShowLottery: broadcasting EventShowLotteryPacket. playersWonItemsMatrix: '%s'", Arrays.toString(playersWonItemsMatrix)));
        Packet packet = new EventShowLotteryPacket(playersWonItemsMatrix);
        networkManager.broadcastBytePayload(packet.getBuiltPacket());
    }

    /**
     * Method used to broadcast a EventShowGameResultsPacket to all connected endpoints
     */
    public void broadcastShowGameResults() {
        Log.i(TAG, "broadcastShowGameResults: broadcasting EventShowGameResultsPacket.");
        Packet packet = new EventShowGameResultsPacket();
        networkManager.broadcastBytePayload(packet.getBuiltPacket());
    }

    /**
     * Method used to broadcast a EventCategoryPlayerVotePacket to all connected endpoints
     *
     * @param targetPlayerId String of affected player's id
     * @param categoryId     String voted category id
     */
    public void broadcastCategoryPlayerVote(String targetPlayerId, String categoryId) {
        Log.i(TAG, String.format("broadcastCategoryPlayerVote: broadcasting EventCategoryPlayerVotePacket. targetPlayerId: '%s', categoryId: '%s'", targetPlayerId, categoryId));
        Packet packet = new EventCategoryPlayerVotePacket(targetPlayerId, categoryId);
        networkManager.broadcastBytePayload(packet.getBuiltPacket());
    }

    /**
     * Method used to broadcast a EventCategoryVoteResultPacket to all connected endpoints
     *
     * @param categoryId String category id of the category vote result
     */
    public void broadcastCategoryVoteResult(String categoryId) {
        Log.i(TAG, String.format("broadcastCategoryVoteResult: broadcasting EventCategoryVoteResultPacket. categoryId: '%s'", categoryId));
        Packet packet = new EventCategoryVoteResultPacket(categoryId);
        networkManager.broadcastBytePayload(packet.getBuiltPacket());
    }

    /**
     * Method used to broadcast a EventPlayerAnsweredQuestionPacket to all connected endpoints
     *
     * @param targetPlayerId String the affected player's id
     * @param categoryId     String the answered question's category id
     * @param questionId     String the answered questions's id
     * @param givenAnswer    String the answered answer text
     * @param timeLeft       String containing the timeLeft of type long converted to a string
     */
    public void broadcastPlayerAnsweredQuestion(String targetPlayerId, String categoryId,
                                                String questionId, String givenAnswer,
                                                String timeLeft) {
        Log.i(TAG, String.format("broadcastPlayerAnsweredQuestion: broadcasting " +
                "EventPlayerAnsweredQuestionPacket. targetPlayerId: %s, " +
                "categoryId: '%s', questionId: '%s', givenAnswer: '%s', " +
                "timeLeft: '%s'", targetPlayerId, categoryId, questionId, givenAnswer, timeLeft));

        Packet packet = new EventPlayerAnsweredQuestionPacket(targetPlayerId, categoryId,
                questionId, givenAnswer, timeLeft);
        networkManager.broadcastBytePayload(packet.getBuiltPacket());
    }
}
