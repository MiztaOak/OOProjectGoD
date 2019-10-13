package com.god.kahit.networkManager;

import android.util.Log;

import com.god.kahit.networkManager.Callbacks.ClientRequestsCallback;
import com.god.kahit.networkManager.Callbacks.HostEventCallback;
import com.god.kahit.networkManager.Packets.EventGameStartedPacket;
import com.god.kahit.networkManager.Packets.EventLobbyReadyChangePacket;
import com.god.kahit.networkManager.Packets.EventLobbySyncEndPacket;
import com.god.kahit.networkManager.Packets.EventLobbySyncStartPacket;
import com.god.kahit.networkManager.Packets.EventPlayerChangeTeamPacket;
import com.god.kahit.networkManager.Packets.EventPlayerJoinedPacket;
import com.god.kahit.networkManager.Packets.EventPlayerLeftPacket;
import com.god.kahit.networkManager.Packets.EventPlayerNameChangePacket;
import com.god.kahit.networkManager.Packets.EventTeamCreatedPacket;
import com.god.kahit.networkManager.Packets.EventTeamDeletedPacket;
import com.god.kahit.networkManager.Packets.EventTeamNameChangePacket;
import com.god.kahit.networkManager.Packets.Packet;
import com.god.kahit.networkManager.Packets.PlayerIdPacket;
import com.god.kahit.networkManager.Packets.RequestLobbyReadyChangePacket;
import com.god.kahit.networkManager.Packets.RequestPlayerChangeTeamPacket;
import com.god.kahit.networkManager.Packets.RequestPlayerNameChangePacket;
import com.god.kahit.networkManager.Packets.RequestTeamNameChangePacket;

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

            case (RequestLobbyReadyChangePacket.PACKET_ID): //5
                handleRequestLobbyReadyChangePacket(id, payload);
                break;

            case (EventLobbyReadyChangePacket.PACKET_ID): //6
                handleEventLobbyReadyChangePacket(payload);
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

            default:
                Log.i(TAG, String.format("handleReceivedPayload: Unknown packetID from: '%s', " +
                        "packetId:'%s'", id, receivedPacketID));
        }
    }

    // ====================== Handle methods ======================

    private void handlePlayerIdPacket(String senderId, byte[] payload) {
        String playerId = PlayerIdPacket.getPlayerId(payload);
        Log.i(TAG, String.format("handlePlayerIdPacket: Received new playerId: '%s'", playerId));
        networkManager.setPlayerId(playerId);

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

    private void handleRequestLobbyReadyChangePacket(String senderId, byte[] payload) {
        boolean newState = RequestLobbyReadyChangePacket.getNewState(payload);
        Log.i(TAG, String.format("RequestLobbyReadyChangePacket: Received player ready state change request from '%s'. New state is '%s'", senderId, newState));

        if (clientRequestsCallback != null) {
            clientRequestsCallback.onLobbyReadyChangeRequest(senderId, newState);
        }
    }

    private void handleEventLobbyReadyChangePacket(byte[] payload) {
        String targetPlayerId = EventLobbyReadyChangePacket.getTargetPlayerId(payload);
        boolean newState = EventLobbyReadyChangePacket.getNewState(payload);
        Log.i(TAG, String.format("handleEventLobbyReadyChangePacket: Received LobbyReadyChange. targetPlayerId: '%s', new state: '%s'", targetPlayerId, newState));
        if (hostEventCallback != null) {
            hostEventCallback.onLobbyReadyChangeEvent(targetPlayerId, newState);
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

    // ====================== Send methods ======================

    public void sendPlayerId(Connection connection, String playerId) {
        Log.i(TAG, String.format("sendPlayerId: sending PlayerIdPacket to '%s'", playerId));
        Packet packet = new PlayerIdPacket(playerId);
        networkManager.sendBytePayload(connection, packet.getBuiltPacket());
    }

    public void sendRequestPlayerNameChange(String newName) {
        Log.i(TAG, String.format("sendRequestPlayerNameChange: sending RequestPlayerNameChangePacket to host: '%s'", networkManager.getConnectionHost().getId()));
        Packet packet = new RequestPlayerNameChangePacket(newName);
        networkManager.sendBytePayload(networkManager.getConnectionHost(), packet.getBuiltPacket());
    }

    public void sendRequestReadyStatus(boolean newState) {
        Log.i(TAG, String.format("sendRequestReadyStatus: sending RequestLobbyReadyChangePacket to host: '%s'", networkManager.getConnectionHost().getId()));
        Packet packet = new RequestLobbyReadyChangePacket(newState);
        networkManager.sendBytePayload(networkManager.getConnectionHost(), packet.getBuiltPacket());
    }

    public void sendRequestTeamNameChange(String teamId, String newTeamName) {
        Log.i(TAG, String.format("sendRequestTeamNameChange: sending RequestTeamNameChangePacket to host: '%s'", networkManager.getConnectionHost().getId()));
        Packet packet = new RequestTeamNameChangePacket(teamId, newTeamName);
        networkManager.sendBytePayload(networkManager.getConnectionHost(), packet.getBuiltPacket());
    }

    public void sendRequestTeamChange(String newTeamID) {
        Log.i(TAG, String.format("sendRequestTeamChange: sending RequestPlayerChangeTeamPacket to host: '%s'", networkManager.getConnectionHost().getId()));
        Packet packet = new RequestPlayerChangeTeamPacket(newTeamID);
        networkManager.sendBytePayload(networkManager.getConnectionHost(), packet.getBuiltPacket());
    }

    public void sendRequestBuyItem(String itemID) {
        //todo implement
    }

    public void sendRequestAnswerQuestion(String answerID) {
        //todo implement
    }

    // ====================== Broadcast methods ======================

    public void broadcastLobbySyncStartPacket(String targetPlayerId, String roomName, String gameModeId) {
        Log.i(TAG, String.format("broadcastLobbySyncStartPacket: broadcasting " +
                "EventLobbySyncStartPacket. targetPlayerId: '%s', roomName: '%s', " +
                "gameModeId: '%s'", targetPlayerId, roomName, gameModeId));

        //Start queuing incoming payloads
        networkManager.setQueueIncomingPayloads(true);

        //Send packet
        Packet packet = new EventLobbySyncStartPacket(targetPlayerId, roomName, gameModeId);
        networkManager.broadcastBytePayload(packet.getBuiltPacket());
    }

    public void broadcastLobbySyncEndPacket() {
        Log.i(TAG, "broadcastLobbySyncEndPacket: broadcasting EventLobbySyncEndPacket.");
        Packet packet = new EventLobbySyncEndPacket();
        networkManager.broadcastBytePayload(packet.getBuiltPacket());

        //As sync is complete, process received payloads and reset setQueueIncomingPayloads
        networkManager.processPayloadQueue();
    }

    public void broadcastPlayerNameChange(String targetPlayerId, String newName) {
        Log.i(TAG, String.format("broadcastPlayerNameChange: broadcasting" +
                " EventPlayerNameChangePacket. targetPlayerId: '%s', " +
                "newName: '%s'", targetPlayerId, newName));
        Packet packet = new EventPlayerNameChangePacket(targetPlayerId, newName);
        networkManager.broadcastBytePayload(packet.getBuiltPacket());
    }

    public void broadcastLobbyReadyChange(String targetPlayerId, boolean newState) {
        Log.i(TAG, String.format("broadcastLobbyReadyChange: broadcasting EventLobbyReadyChangePacket. targetPlayerId: '%s', newState: '%s'", targetPlayerId, newState));
        Packet packet = new EventLobbyReadyChangePacket(targetPlayerId, newState);
        networkManager.broadcastBytePayload(packet.getBuiltPacket());
    }

    public void broadcastTeamNameChange(String teamId, String newTeamName) {
        Log.i(TAG, String.format("broadcastTeamNameChange: broadcasting EventTeamNameChangePacket. teamId: '%s', newTeamName: '%s'", teamId, newTeamName));
        Packet packet = new EventTeamNameChangePacket(teamId, newTeamName);
        networkManager.broadcastBytePayload(packet.getBuiltPacket());
    }

    public void broadcastPlayerJoined(String playerId, String playerName) {
        Log.i(TAG, String.format("broadcastPlayerJoined: broadcasting EventPlayerJoinedPacket. playerId: '%s', playerName: '%s'", playerId, playerName));
        Packet packet = new EventPlayerJoinedPacket(playerId, playerName);
        networkManager.broadcastBytePayload(packet.getBuiltPacket());
    }

    public void broadcastPlayerLeft(String playerId) {
        Log.i(TAG, String.format("broadcastPlayerLeft: broadcasting EventPlayerLeftPacket. playerId: '%s'", playerId));
        Packet packet = new EventPlayerLeftPacket(playerId);
        networkManager.broadcastBytePayload(packet.getBuiltPacket());
    }

    public void broadcastPlayerChangeTeam(String targetPlayerId, String newTeamId) {
        Log.i(TAG, String.format("broadcastPlayerTeamChange: broadcasting EventPlayerChangeTeamPacket. targetPlayerId: '%s', newTeamId: '%s'", targetPlayerId, newTeamId));
        Packet packet = new EventPlayerChangeTeamPacket(targetPlayerId, newTeamId);
        networkManager.broadcastBytePayload(packet.getBuiltPacket());
    }

    public void broadcastTeamCreated(String newTeamId, String newTeamName) {
        Log.i(TAG, String.format("broadcastTeamCreated: broadcasting EventTeamCreatedPacket. newTeamId: '%s', newTeamName: '%s'", newTeamId, newTeamName));
        Packet packet = new EventTeamCreatedPacket(newTeamId, newTeamName);
        networkManager.broadcastBytePayload(packet.getBuiltPacket());
    }

    public void broadcastTeamDeleted(String teamId) {
        Log.i(TAG, String.format("broadcastTeamDeleted: broadcasting EventTeamDeletedPacket. teamId: '%s'", teamId));
        Packet packet = new EventTeamDeletedPacket(teamId);
        networkManager.broadcastBytePayload(packet.getBuiltPacket());
    }

    public void broadcastGameStarted() {
        Log.i(TAG, "broadcastGameStarted: broadcasting EventGameStartedPacket.");
        Packet packet = new EventGameStartedPacket();
        networkManager.broadcastBytePayload(packet.getBuiltPacket());
    }

}
