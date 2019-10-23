package com.god.kahit.networkManager;

import com.god.kahit.networkManager.Callbacks.ClientRequestsCallback;
import com.god.kahit.networkManager.Callbacks.HostEventCallback;
import com.god.kahit.networkManager.Packets.EventCategoryPlayerVotePacket;
import com.god.kahit.networkManager.Packets.EventCategoryVoteResultPacket;
import com.god.kahit.networkManager.Packets.EventGameStartedPacket;
import com.god.kahit.networkManager.Packets.EventLobbySyncEndPacket;
import com.god.kahit.networkManager.Packets.EventLobbySyncStartPacket;
import com.god.kahit.networkManager.Packets.EventPlayerAnsweredQuestionPacket;
import com.god.kahit.networkManager.Packets.EventPlayerChangeTeamPacket;
import com.god.kahit.networkManager.Packets.EventPlayerJoinedPacket;
import com.god.kahit.networkManager.Packets.EventPlayerLeftPacket;
import com.god.kahit.networkManager.Packets.EventPlayerNameChangePacket;
import com.god.kahit.networkManager.Packets.EventPlayerReadyChangePacket;
import com.god.kahit.networkManager.Packets.EventShowCategorySelectionPacket;
import com.god.kahit.networkManager.Packets.EventShowGameResultsPacket;
import com.god.kahit.networkManager.Packets.EventShowLotteryPacket;
import com.god.kahit.networkManager.Packets.EventShowQuestionPacket;
import com.god.kahit.networkManager.Packets.EventShowRoundStatsPacket;
import com.god.kahit.networkManager.Packets.EventTeamCreatedPacket;
import com.god.kahit.networkManager.Packets.EventTeamDeletedPacket;
import com.god.kahit.networkManager.Packets.EventTeamNameChangePacket;
import com.god.kahit.networkManager.Packets.Packet;
import com.god.kahit.networkManager.Packets.PlayerIdPacket;
import com.god.kahit.networkManager.Packets.RequestCategoryPlayerVotePacket;
import com.god.kahit.networkManager.Packets.RequestPlayerAnswerQuestionPacket;
import com.god.kahit.networkManager.Packets.RequestPlayerChangeTeamPacket;
import com.god.kahit.networkManager.Packets.RequestPlayerNameChangePacket;
import com.god.kahit.networkManager.Packets.RequestPlayerReadyChangePacket;
import com.god.kahit.networkManager.Packets.RequestTeamNameChangePacket;

import java.util.Arrays;

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
     * Update this accordingly to represent all implemented packets.
     * @param id connection id of sender
     * @param payload received payload
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
        }
    }

    // ====================== Handle methods ======================

    private void handlePlayerIdPacket(String senderId, byte[] payload) {
        String playerId = PlayerIdPacket.getPlayerId(payload);
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

        if (hostEventCallback != null) {
            hostEventCallback.onLobbySyncStartEvent(targetPlayerId, roomName, gameModeId);
        }
    }

    private void handleLobbySyncEndPacket() {
        if (hostEventCallback != null) {
            hostEventCallback.onLobbySyncEndEvent();
        }
    }

    private void handleRequestPlayerNameChangePacket(String senderId, byte[] payload) {
        String newName = RequestPlayerNameChangePacket.getNewPlayerName(payload);

        if (clientRequestsCallback != null) {
            clientRequestsCallback.onPlayerNameChangeRequest(senderId, newName);
        }
    }

    private void handleEventPlayerNameChangePacket(byte[] payload) {
        String targetPlayerId = EventPlayerNameChangePacket.getTargetPlayerId(payload);
        String newName = EventPlayerNameChangePacket.getNewPlayerName(payload);

        if (hostEventCallback != null) {
            hostEventCallback.onPlayerNameChangeEvent(targetPlayerId, newName);
        }
    }

    private void handleRequestPlayerReadyChangePacket(String senderId, byte[] payload) {
        boolean newState = RequestPlayerReadyChangePacket.getNewState(payload);


        if (clientRequestsCallback != null) {
            clientRequestsCallback.onPlayerReadyChangeRequest(senderId, newState);
        }
    }

    private void handleEventPlayerReadyChangePacket(byte[] payload) {
        String targetPlayerId = EventPlayerReadyChangePacket.getTargetPlayerId(payload);
        boolean newState = EventPlayerReadyChangePacket.getNewState(payload);

        if (hostEventCallback != null) {
            hostEventCallback.onPlayerReadyChangeEvent(targetPlayerId, newState);
        }
    }

    private void handleRequestTeamNameChangePacket(String senderId, byte[] payload) {
        String teamId = RequestTeamNameChangePacket.getTeamId(payload);
        String newTeamName = RequestTeamNameChangePacket.getNewTeamName(payload);


        if (clientRequestsCallback != null) {
            clientRequestsCallback.onTeamNameChangeRequest(teamId, newTeamName);
        }
    }

    private void handleEventTeamNameChangePacket(byte[] payload) {
        String teamId = EventTeamNameChangePacket.getTeamId(payload);
        String newTeamName = EventTeamNameChangePacket.getNewTeamName(payload);


        if (hostEventCallback != null) {
            hostEventCallback.onTeamNameChangeEvent(teamId, newTeamName);
        }
    }

    private void handleEventPlayerJoinedPacket(byte[] payload) {
        String playerId = EventPlayerJoinedPacket.getPlayerId(payload);
        String playerName = EventPlayerJoinedPacket.getPlayerName(payload);


        if (hostEventCallback != null) {
            hostEventCallback.onPlayerJoinedEvent(playerId, playerName);
        }
    }

    private void handleEventPlayerLeftPacket(byte[] payload) {
        String playerId = EventPlayerLeftPacket.getPlayerId(payload);


        if (hostEventCallback != null) {
            hostEventCallback.onPlayerLeftEvent(playerId);
        }
    }

    private void handleRequestPlayerChangeTeamPacket(String senderId, byte[] payload) {
        String newTeamId = RequestPlayerChangeTeamPacket.getNewTeamId(payload);


        if (clientRequestsCallback != null) {
            clientRequestsCallback.onPlayerTeamChangeRequest(senderId, newTeamId);
        }
    }

    private void handleEventPlayerChangeTeamPacket(byte[] payload) {
        String targetPlayerId = EventPlayerChangeTeamPacket.getTargetPlayerId(payload);
        String newTeamId = EventPlayerChangeTeamPacket.getNewTeamId(payload);


        if (hostEventCallback != null) {
            hostEventCallback.onPlayerChangeTeamEvent(targetPlayerId, newTeamId);
        }
    }

    private void handleEventTeamCreatedPacket(byte[] payload) {
        String newTeamId = EventTeamCreatedPacket.getNewTeamId(payload);
        String newTeamName = EventTeamCreatedPacket.getNewTeamName(payload);


        if (hostEventCallback != null) {
            hostEventCallback.onTeamCreatedEvent(newTeamId, newTeamName);
        }
    }

    private void handleEventTeamDeletedPacket(byte[] payload) {
        String teamId = EventTeamDeletedPacket.getTeamId(payload);


        if (hostEventCallback != null) {
            hostEventCallback.onTeamDeletedEvent(teamId);
        }
    }

    private void handleEventGameStartedPacket() {
        if (hostEventCallback != null) {
            hostEventCallback.onGameStartedEvent();
        }
    }

    private void handleEventShowQuestionPacket(byte[] payload) {
        String categoryId = EventShowQuestionPacket.getCategoryId(payload);
        String questionId = EventShowQuestionPacket.getQuestionId(payload);

        if (hostEventCallback != null) {
            hostEventCallback.onShowQuestionEvent(categoryId, questionId);
        }
    }

    private void handleEventShowRoundStatsPacket() {
        if (hostEventCallback != null) {
            hostEventCallback.onShowRoundStatsEvent();
        }
    }

    private void handleEventShowCategorySelectionPacket(byte[] payload) {
        String[] categoryIds = EventShowCategorySelectionPacket.getCategoryIds(payload);

        if (hostEventCallback != null) {
            hostEventCallback.onShowCategorySelectionEvent(categoryIds);
        }
    }

    private void handleEventShowLotteryPacket(byte[] payload) {
        String[][] playersWonItemsMatrix = EventShowLotteryPacket.getPlayersWonItemsMatrix(payload);

        if (hostEventCallback != null) {
            hostEventCallback.onShowLotteryEvent(playersWonItemsMatrix);
        }
    }

    private void handleEventShowGameResultsPacket() {
        if (hostEventCallback != null) {
            hostEventCallback.onShowGameResultsEvent();
        }
    }

    private void handleEventCategoryPlayerVotePacket(byte[] payload) {
        String targetPlayerId = EventCategoryPlayerVotePacket.getTargetPlayerId(payload);
        String categoryId = EventCategoryPlayerVotePacket.getCategoryId(payload);

        if (hostEventCallback != null) {
            hostEventCallback.onCategoryPlayerVoteEvent(targetPlayerId, categoryId);
        }
    }

    private void handleRequestCategoryPlayerVotePacket(String senderId, byte[] payload) {
        String categoryId = RequestCategoryPlayerVotePacket.getCategoryId(payload);

        if (clientRequestsCallback != null) {
            clientRequestsCallback.onCategoryPlayerVoteRequest(senderId, categoryId);
        }
    }

    private void handleEventCategoryVoteResultPacket(byte[] payload) {
        String categoryId = EventCategoryVoteResultPacket.getCategoryId(payload);

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

        if (clientRequestsCallback != null) {
            clientRequestsCallback.onPlayerAnsweredQuestionRequest(senderId, categoryId, questionId,
                    givenAnswer, Long.valueOf(timeLeft));
        }
    }

    // ====================== Send methods ======================

    public void sendPlayerId(Connection connection, String playerId) {
        Packet packet = new PlayerIdPacket(playerId);
        networkManager.sendBytePayload(connection, packet.getBuiltPacket());
    }

    public void sendRequestPlayerNameChange(String newName) {
        Packet packet = new RequestPlayerNameChangePacket(newName);
        networkManager.sendBytePayload(networkManager.getConnectionHost(), packet.getBuiltPacket());
    }

    public void sendRequestReadyStatus(boolean newState) {
        Packet packet = new RequestPlayerReadyChangePacket(newState);
        networkManager.sendBytePayload(networkManager.getConnectionHost(), packet.getBuiltPacket());
    }

    public void sendRequestTeamNameChange(String teamId, String newTeamName) {
        Packet packet = new RequestTeamNameChangePacket(teamId, newTeamName);
        networkManager.sendBytePayload(networkManager.getConnectionHost(), packet.getBuiltPacket());
    }

    public void sendRequestTeamChange(String newTeamID) {
        Packet packet = new RequestPlayerChangeTeamPacket(newTeamID);
        networkManager.sendBytePayload(networkManager.getConnectionHost(), packet.getBuiltPacket());
    }

    public void sendRequestBuyItem(String itemID) {
        //todo implement
    }

    public void sendRequestAnswerQuestion(String categoryId, String questionId, String givenAnswer, String timeLeft) {
        Packet packet = new RequestPlayerAnswerQuestionPacket(categoryId, questionId, givenAnswer, timeLeft);
        networkManager.sendBytePayload(networkManager.getConnectionHost(), packet.getBuiltPacket());
    }

    public void sendRequestCategoryVote(String categoryId) {
        Packet packet = new RequestCategoryPlayerVotePacket(categoryId);
        networkManager.sendBytePayload(networkManager.getConnectionHost(), packet.getBuiltPacket());
    }

    // ====================== Broadcast methods ======================

    public void broadcastLobbySyncStartPacket(String targetPlayerId, String roomName,
                                              String gameModeId) {

        //Start queuing incoming payloads
        networkManager.setQueueIncomingPayloads(true);

        //Send packet
        Packet packet = new EventLobbySyncStartPacket(targetPlayerId, roomName, gameModeId);
        networkManager.broadcastBytePayload(packet.getBuiltPacket());
    }

    public void broadcastLobbySyncEndPacket() {
        Packet packet = new EventLobbySyncEndPacket();
        networkManager.broadcastBytePayload(packet.getBuiltPacket());

        //As sync is complete, process received payloads and reset setQueueIncomingPayloads
        networkManager.processPayloadQueue();
    }

    public void broadcastPlayerNameChange(String targetPlayerId, String newName) {
        Packet packet = new EventPlayerNameChangePacket(targetPlayerId, newName);
        networkManager.broadcastBytePayload(packet.getBuiltPacket());
    }

    public void broadcastPlayerReadyChange(String targetPlayerId, boolean newState) {
        Packet packet = new EventPlayerReadyChangePacket(targetPlayerId, newState);
        networkManager.broadcastBytePayload(packet.getBuiltPacket());
    }

    public void broadcastTeamNameChange(String teamId, String newTeamName) {
        Packet packet = new EventTeamNameChangePacket(teamId, newTeamName);
        networkManager.broadcastBytePayload(packet.getBuiltPacket());
    }

    public void broadcastPlayerJoined(String playerId, String playerName) {
        Packet packet = new EventPlayerJoinedPacket(playerId, playerName);
        networkManager.broadcastBytePayload(packet.getBuiltPacket());
    }

    public void broadcastPlayerLeft(String playerId) {
        Packet packet = new EventPlayerLeftPacket(playerId);
        networkManager.broadcastBytePayload(packet.getBuiltPacket());
    }

    public void broadcastPlayerChangeTeam(String targetPlayerId, String newTeamId) {
        Packet packet = new EventPlayerChangeTeamPacket(targetPlayerId, newTeamId);
        networkManager.broadcastBytePayload(packet.getBuiltPacket());
    }

    public void broadcastTeamCreated(String newTeamId, String newTeamName) {
        Packet packet = new EventTeamCreatedPacket(newTeamId, newTeamName);
        networkManager.broadcastBytePayload(packet.getBuiltPacket());
    }

    public void broadcastTeamDeleted(String teamId) {
        Packet packet = new EventTeamDeletedPacket(teamId);
        networkManager.broadcastBytePayload(packet.getBuiltPacket());
    }

    public void broadcastGameStarted() {
        Packet packet = new EventGameStartedPacket();
        networkManager.broadcastBytePayload(packet.getBuiltPacket());
    }

    public void broadcastShowQuestion(String categoryId, String questionId) {
        Packet packet = new EventShowQuestionPacket(categoryId, questionId);
        networkManager.broadcastBytePayload(packet.getBuiltPacket());
    }

    public void broadcastShowRoundStats() {
        Packet packet = new EventShowRoundStatsPacket();
        networkManager.broadcastBytePayload(packet.getBuiltPacket());
    }

    public void broadcastShowCategorySelection(String[] categoryIds) {
        Packet packet = new EventShowCategorySelectionPacket(categoryIds);
        networkManager.broadcastBytePayload(packet.getBuiltPacket());
    }

    public void broadcastShowLottery(String[][] playersWonItemsMatrix) {
        Packet packet = new EventShowLotteryPacket(playersWonItemsMatrix);
        networkManager.broadcastBytePayload(packet.getBuiltPacket());
    }

    public void broadcastShowGameResults() {
        Packet packet = new EventShowGameResultsPacket();
        networkManager.broadcastBytePayload(packet.getBuiltPacket());
    }

    public void broadcastCategoryPlayerVote(String targetPlayerId, String categoryId) {
        Packet packet = new EventCategoryPlayerVotePacket(targetPlayerId, categoryId);
        networkManager.broadcastBytePayload(packet.getBuiltPacket());
    }

    public void broadcastCategoryVoteResult(String categoryId) {
        Packet packet = new EventCategoryVoteResultPacket(categoryId);
        networkManager.broadcastBytePayload(packet.getBuiltPacket());
    }

    public void broadcastPlayerAnsweredQuestion(String targetPlayerId, String categoryId,
                                                String questionId, String givenAnswer,
                                                String timeLeft) {
        Packet packet = new EventPlayerAnsweredQuestionPacket(targetPlayerId, categoryId,
                questionId, givenAnswer, timeLeft);
        networkManager.broadcastBytePayload(packet.getBuiltPacket());
    }
}