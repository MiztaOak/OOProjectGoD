package com.god.kahit.networkManager;

import android.content.Context;
import android.util.Log;
import android.util.Pair;

import com.god.kahit.networkManager.callbacks.NetworkCallback;
import com.god.kahit.networkManager.packets.EventLobbySyncEndPacket;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.connection.AdvertisingOptions;
import com.google.android.gms.nearby.connection.ConnectionInfo;
import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback;
import com.google.android.gms.nearby.connection.ConnectionResolution;
import com.google.android.gms.nearby.connection.ConnectionsClient;
import com.google.android.gms.nearby.connection.DiscoveredEndpointInfo;
import com.google.android.gms.nearby.connection.DiscoveryOptions;
import com.google.android.gms.nearby.connection.EndpointDiscoveryCallback;
import com.google.android.gms.nearby.connection.Payload;
import com.google.android.gms.nearby.connection.PayloadCallback;
import com.google.android.gms.nearby.connection.PayloadTransferUpdate;
import com.google.android.gms.nearby.connection.Strategy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;

import androidx.annotation.NonNull;

/**
 * @responsibility: This class is responsible for handling the low level network implementation
 * details and communication with the Nearby Connections API. The class is built to support
 * a facade pattern by implementing the NetworkManager interface.
 * @used-by: This class is used in the following classes:
 * Repository
 * @author: Mats Cedervall
 */
public class NetworkModule implements NetworkManager {
    private static final String TAG = "NetworkModule";
    private static final int MAX_NMB_CONNECTIONS = 99;
    private static final int MAX_PAYLOAD_QUEUE_SIZE = 9999;
    private static final int MAX_PLAYERNAME_LENGTH = 12;

    private static final Strategy STRATEGY = Strategy.P2P_STAR;
    private static NetworkModule networkModule;
    private ConnectionsClient connectionsClient;     // Our handle to Nearby Connections
    private String connectionName;
    private String playerId;

    private boolean isHost;
    private boolean isScanning;
    private boolean isHostBeaconActive;
    private boolean isQueuingIncomingPayloads;
    private NetworkCallback networkCallback;
    private ConnectionLifecycleCallback connectionLifecycleCallback;
    private EndpointDiscoveryCallback endpointDiscoveryCallback;
    private PayloadCallback payloadCallback;
    private LinkedHashMap<String, Connection> connectionLinkedHashMap;
    private List<Pair<String, byte[]>> payloadQueueList;
    private Context context;

    private NetworkModule() {

    }

    public static NetworkModule getInstance(Context context, NetworkCallback networkCallback) {
        if (networkModule == null) {
            networkModule = new NetworkModule();
            networkModule.init(context, networkCallback);
        }
        return networkModule;
    }

    private void init(Context context, NetworkCallback networkCallback) {
        this.context = context;
        this.networkCallback = networkCallback;
        connectionName = "Default name";
        connectionLinkedHashMap = new LinkedHashMap<>();
        payloadQueueList = new ArrayList<>();
        isHost = false;
        isScanning = false;
        isHostBeaconActive = false;
        isQueuingIncomingPayloads = false;
        connectionsClient = Nearby.getConnectionsClient(context);

        setupCallbacks();
        Log.i(TAG, "init: a new networkModule was initialized ");
    }

    private void setupCallbacks() {
        endpointDiscoveryCallback = new EndpointDiscoveryCallback() {
            @Override
            public void onEndpointFound(@NonNull String s, @NonNull DiscoveredEndpointInfo discoveredEndpointInfo) {
                Log.i(TAG, "onEndpointFound: endpoint found: s:'" + s +
                        "' endpointName: '" + discoveredEndpointInfo.getEndpointName() +
                        "' serviceID: '" + discoveredEndpointInfo.getServiceId() + "'.");

                if (!context.getPackageName().equals(discoveredEndpointInfo.getServiceId())) {
                    Log.i(TAG, "onEndpointFound: endpoint serviceID mismatch found, is this another application using Nearby-API? Ignoring endpoint");
                    return;
                }

                //Store connection details
                Connection connection = connectionLinkedHashMap.get(s);
                if (connection != null) {
                    if (!connection.getState().isDisconnected()) {
                        Log.i(TAG, "onEndpointFound: an existing active connection was found, ignoring it: " + s);
                    } else {
                        connection.setType(ConnectionType.SERVER);
                        connection.setState(ConnectionState.UNCONNECTED);
                    }
                } else {
                    connection = new Connection(s,
                            discoveredEndpointInfo.getEndpointName(), ConnectionType.SERVER,
                            ConnectionState.UNCONNECTED);
                    addConnection(connection);
                }
                //Pass on callback
                networkCallback.onHostFound(s, connection);
            }

            @Override
            public void onEndpointLost(@NonNull String s) {
                Log.i(TAG, "onEndpointLost: endpoint lost: " + s);
                Connection connection = connectionLinkedHashMap.get(s);
                if (connection != null) {
                    //Update stored connection status
                    ConnectionState oldState = connection.getState();
                    connection.setState(ConnectionState.DISCONNECTED);

                    //Pass on callback
                    networkCallback.onConnectionChanged(connection, oldState, connection.getState());
                    networkCallback.onHostLost(s);
                } else {
                    Log.i(TAG, "onEndpointLost: no valid connection with given id was found");
                }
            }
        };

        connectionLifecycleCallback = new ConnectionLifecycleCallback() {
            @Override
            public void onConnectionInitiated(@NonNull String s, @NonNull ConnectionInfo connectionInfo) {
                Connection connection = connectionLinkedHashMap.get(s);

                if (!isValidConnection(connection, connectionInfo)) {
                    Log.i(TAG, "onConnectionInitiated: ERROR incoming connection-request was invalid. Me as a client was not expecting this connection to be established, ignoring it: " + s);
                    return;
                }

                if (isHostBeaconActive || !isHost) { //If client, accept your own request as it was expected
                    Log.i(TAG, "onConnectionInitiated: accepting connection: '" + s + "', '" +
                            connectionInfo.getEndpointName() + "' + isIncoming: '" +
                            connectionInfo.isIncomingConnection() + "'.");

                    //Store connection details if new connection, else connectToHost() has been called, and state already been updated.
                    if (connection == null) { //todo as connection is initially marked a 'client' inside host at first connection, a potential re-connection can easily be distinguished (if re-connecting then type must be PEER)
                        connection = new Connection(s,
                                connectionInfo.getEndpointName(), ConnectionType.CLIENT,
                                ConnectionState.CONNECTING);
                        addConnection(connection);
                    }

                    //As host, accept connecting clients when hosting beacon is active.
                    //As client, always accept
                    connectionsClient.acceptConnection(s, payloadCallback);

                    //Pass on callback
                    networkCallback.onClientFound(s, connection);
                } else {
                    Log.i(TAG, "onConnectionInitiated: ERROR declining connection due inactive host beacon: " + s);

                    //Store connection details
                    if (connection == null) {
                        connection = new Connection(s,
                                connectionInfo.getEndpointName(), ConnectionType.CLIENT,
                                ConnectionState.REJECTED);
                        addConnection(connection);

                        //Pass on callback - new connection
                        networkCallback.onClientFound(s, connection);
                    } else {
                        ConnectionState oldState = connection.getState();
                        connection.setState(ConnectionState.REJECTED);

                        //Pass callback - old connection
                        networkCallback.onConnectionChanged(connection, oldState, connection.getState());
                    }

                    connectionsClient.rejectConnection(s);
                }
            }

            @Override
            public void onConnectionResult(@NonNull String s, @NonNull ConnectionResolution connectionResolution) {
                if (connectionResolution.getStatus().isSuccess()) {
                    Log.i(TAG, "onConnectionResult: connection successful: " + s);

                    Connection connection = connectionLinkedHashMap.get(s);
                    if (connection != null) {
                        //Update stored connection status
                        connection.setType(ConnectionType.PEER); //At this point on, the two peers are identical (No server / client)
                        ConnectionState oldState = connection.getState();
                        connection.setState(ConnectionState.CONNECTED);

                        //stop discovery
                        if (isScanning) {
                            stopScan();
                        }

                        if (oldState.equals(ConnectionState.DISCONNECTING)) {
                            Log.i(TAG, "onConnectionResult: ERROR connecting device was already set for disconnection. Disconnecting..");
                            disconnect(connection);
                        } else {
                            //Pass on callback as all is well
                            networkCallback.onConnectionChanged(connection, oldState, connection.getState());
                            networkCallback.onConnectionEstablished(s, connection);
                        }
                    } else {
                        Log.i(TAG, "onConnectionResult: ERROR no valid connection with given id was found");
                    }
                } else {
                    Connection connection = connectionLinkedHashMap.get(s);
                    if (connection != null) {
                        Log.i(TAG, "onConnectionResult: ERROR connection failed, with known id: '" + s + "', result: '" + connectionResolution.getStatus().toString() + "'.");

                        //Update stored connection status
                        ConnectionState oldState = connection.getState();
                        connection.setState(ConnectionState.DISCONNECTED);

                        //Pass on callback as all is well
                        networkCallback.onConnectionChanged(connection, oldState, connection.getState());
                        networkCallback.onConnectionLost(s);
                    } else {
                        Log.i(TAG, "onConnectionResult: ERROR connection failed, with unknown id: '" + s + "', result: '" + connectionResolution.getStatus().toString() + "'.");
                    }
                }
            }

            @Override
            public void onDisconnected(@NonNull String s) {
                Log.i(TAG, "onDisconnected: a connection was disconnected: " + s);
                Connection connection = connectionLinkedHashMap.get(s);
                if (connection != null) {
                    //Update stored connection status //todo create method for this and callback? frequently used..
                    ConnectionState oldState = connection.getState();
                    connection.setState(ConnectionState.DISCONNECTED);

                    //Pass on callback
                    networkCallback.onConnectionChanged(connection, oldState, connection.getState());
                    networkCallback.onConnectionLost(s);
                } else {
                    Log.i(TAG, "onDisconnected: ERROR no valid connection with given id was found");
                }
            }
        };

        payloadCallback = new PayloadCallback() {
            @Override
            public void onPayloadReceived(@NonNull String s, @NonNull Payload payload) { //todo store payloads for debugging reasons?
                if (payload.getType() == Payload.Type.BYTES) {
                    byte[] receivedBytes = payload.asBytes(); // This always gets the full data of the payload. Bytes are always sent in one-go. Will be null if it's not a BYTES.

                    //Check if valid sender connection, else ignore it.
                    Connection connection = connectionLinkedHashMap.get(s);
                    if (connection != null) {
                        if (connection.getState().equals(ConnectionState.CONNECTED)) {
                            Log.i(TAG, "onPayloadReceived: received payload from: '" + s + "'. Payload: " + Arrays.toString(receivedBytes));

                            //Pass on parameters
                            determineQueueOrHandle(s, receivedBytes);
                        } else {
                            Log.i(TAG, String.format("onPayloadReceived: ERROR received payload from a non-connected connection. ignoring: '%s'. State: '%s'. Payload: '%s'", s, connection.getState().toString(), payload.toString()));
                        }
                    } else {
                        Log.i(TAG, String.format("onPayloadReceived: ERROR received payload from unknown connection. ignoring: '%s'. Payload: '%s'", s, payload.toString()));
                    }
                } else if (payload.getType() == Payload.Type.FILE) {
                    //todo implement file transfer? https://developers.google.com/nearby/connections/android/exchange-data
                    Log.i(TAG, "onPayloadReceived: ERROR received file payload, unfortunately" +
                            " support is not implemented so cant handle it. File will be saved in player's download folder");
                } else {
                    Log.i(TAG, "onPayloadReceived: ERROR unknown payload type, probably of stream type");
                }
            }

            @Override
            public void onPayloadTransferUpdate(@NonNull String s, @NonNull PayloadTransferUpdate
                    payloadTransferUpdate) {
                // Bytes payloads are sent as a single chunk, so you'll receive a SUCCESS update immediately
                // after the call to onPayloadReceived().
                // When file transfers are completed you will also receive a SUCCESS update,
                // but content must be saved from onPayloadReceived. See URL in to-do above
                //todo implement file transfer?
            }
        };
    }

    private boolean isValidConnection(Connection connection, ConnectionInfo connectionInfo) {
        //If host and a client has requested a connection
        if (connectionInfo.isIncomingConnection()) {
            return true;
        }

        //If client, connection is known and the establishing connection is expected
        return connection != null && connection.getState().equals(ConnectionState.CONNECTING);
    }

    private void addConnection(Connection connection) {
        //If too many connections, remove first one
        if (connectionLinkedHashMap.size() >= MAX_NMB_CONNECTIONS) {
            Connection firstConnection = connectionLinkedHashMap.values().toArray(new Connection[0])[0];
            disconnect(connectionLinkedHashMap.remove(firstConnection.getId()));
            Log.d(TAG, "addConnection: ERROR maximum number of connections reached (" + MAX_NMB_CONNECTIONS + "), " +
                    "disconnecting first one.");
        }
        connectionLinkedHashMap.put(connection.getId(), connection);
    }

    private void determineQueueOrHandle(@NonNull String s, @NonNull byte[] receivedBytes) {
        int receivedPacketID = Integer.valueOf(Byte.toString(receivedBytes[0])); //todo figure if it can be done without dependency

        if (isQueuingIncomingPayloads && receivedPacketID != EventLobbySyncEndPacket.PACKET_ID) { //todo figure if it can be done without dependency
            //Add payload data to payloadQueueList, remove oldest entry if max payload queue size has been reached
            if (payloadQueueList.size() > MAX_PAYLOAD_QUEUE_SIZE) {
                Log.i(TAG, String.format("determineQueueOrHandle: ERROR maximum queue size reached: '%s', removing oldest payload.", MAX_PAYLOAD_QUEUE_SIZE));
                payloadQueueList.remove(0);
            }
            Log.i(TAG, String.format("determineQueueOrHandle: Queued received payload from: '%s'. New queue size: '%s'", s, payloadQueueList.size() + 1));
            payloadQueueList.add(new Pair<>(s, receivedBytes));
        } else {
            //Pass on callback
            networkCallback.onBytePayloadReceived(s, receivedBytes);
        }
    }

    /**
     * Method that loops through the payloadQueueList and triggers a onBytePayloadReceived
     * callback on each one and then removes it. When completed it sets isQueuingIncomingPayloads
     * to false
     */
    @Override
    public void processPayloadQueue() {
        Log.i(TAG, String.format("processPayloadQueue: processing queued payloads." +
                " Queue size: '%s'.", payloadQueueList.size()));

        //Pass a callback on each queued payload
        while (payloadQueueList.size() > 0) {
            Pair<String, byte[]> queuedPayload = payloadQueueList.remove(0);

            //Pass on callback
            networkCallback.onBytePayloadReceived(queuedPayload.first, queuedPayload.second);
        }

        //Reset do queue boolean when empty
        isQueuingIncomingPayloads = false; //todo a tiny risk of a race condition. i.e incoming payload just after while-loop is complete
        Log.i(TAG, "processPayloadQueue: set isQueueIncomingPayloads to false");
    }

    /**
     * Method that clears the payloadQueueList and sets isQueuingIncomingPayloads to false
     */
    @Override
    public void clearPayloadQueue() {
        Log.i(TAG, String.format("clearPayloadQueue: cleared queued payloads. " +
                "Number of cleared payloads: '%s'", payloadQueueList.size()));
        isQueuingIncomingPayloads = false;
        payloadQueueList.clear();
    }

    private void startAdvertising() {
        connectionsClient.stopAdvertising();
        connectionsClient.startAdvertising(
                connectionName, context.getPackageName(), connectionLifecycleCallback,
                new AdvertisingOptions.Builder().setStrategy(STRATEGY).build());
    }

    private void startDiscovery() {
        connectionsClient.stopDiscovery();
        connectionsClient.startDiscovery(
                context.getPackageName(), endpointDiscoveryCallback,
                new DiscoveryOptions.Builder().setStrategy(STRATEGY).build());
    }

    /**
     * Method that starts the discovering process of other Nearby Connection endpoints in the
     * close proximity. Stops any ongoing advertising.
     */
    @Override
    public void startScan() {
        Log.i(TAG, "startScan: started scanning for hosts");
        connectionsClient.stopAdvertising();
        isHostBeaconActive = false;
        startDiscovery();
        isScanning = true;
    }

    /**
     * Method that stops the discovering process of other Nearby Connection endpoints
     */
    @Override
    public void stopScan() {
        Log.i(TAG, "stopScan: stopped scanning for hosts");
        connectionsClient.stopDiscovery();
        isScanning = false;
    }

    private Connection getConnectedPeer() {
        for (Connection connection : connectionLinkedHashMap.values()) {
            if (connection.getState().equals(ConnectionState.CONNECTED) || connection.getState().equals(ConnectionState.CONNECTING)) {
                return connection;
            }
        }
        return null;
    }

    /**
     * Method used to connect to an advertising device
     *
     * @param connection Connection that holds the details of the endpoint
     */
    @Override
    public void connectToHost(Connection connection) {
        if (isHost) {
            throw new RuntimeException("Tried to connect to server when you are yourself a server. " +
                    "To reset 'isHost' after once starting a hostBeacon, call stopAllConnections() after ensuring a deactivated hostBeacon");
        }

        if (connection == null) {
            Log.i(TAG, "connectToHost: ERROR unable to connect to host, received null connection reference");
            return;
        }

        Connection oldConnection = getConnectedPeer();

        if (oldConnection != null) {
            Log.i(TAG, "connectToHost: ERROR was already connected to: '" + connection.getId() + "'. Disconnecting this old connection");
            disconnect(oldConnection); //todo might cause a race condition, resulting in two connections at the same time - might crash with STAR-strategy?
        }

        Log.i(TAG, "connectToHost: connecting to: " + connection.getId());
        connectionsClient.requestConnection(connectionName, connection.getId(), connectionLifecycleCallback);

        //Update stored state
        ConnectionState oldState = connection.getState();
        connection.setState(ConnectionState.CONNECTING);

        //Perform call back
        networkCallback.onConnectionChanged(connection, oldState, connection.getState());
    }

    /***
     * Method used to start advertising your host endpoint to other devices in the close proximity.
     * Will mark the session as host, call stopAllConnection() to reset this.
     * During a host session any discovering is not allowed due to risk communication interference.
     */
    @Override
    public void startHostBeacon() {
        Log.i(TAG, "startHostBeacon: started host beacon");
        connectionsClient.stopDiscovery();
        isScanning = false;
        isHost = true;
        isHostBeaconActive = true;
        startAdvertising();
    }

    /**
     * Method used to stop an ongoing endpoint advertising. Does not allow any clients to connect
     * when host beacon is off
     */
    @Override
    public void stopHostBeacon() {
        Log.i(TAG, "stopHostBeacon: stopped host beacon");
        isHostBeaconActive = false;
        connectionsClient.stopAdvertising();
    }

    /**
     * Method used to send a byte[] payload to a given connection
     *
     * @param connection Connection with the details of the target endpoint
     * @param payload    byte[] with the content to send
     */
    @Override
    public void sendBytePayload(Connection connection, byte[] payload) {
        if (connection == null) {
            Log.i(TAG, "sendBytePayload: ERROR failed to send payload! null connection given");
            return;
        }

        if (payload == null) {
            Log.i(TAG, "sendBytePayload: ERROR failed to send payload! null payload given, connection: '" + connection.getId() + "'");
            return;
        }

        if (payload.length > ConnectionsClient.MAX_BYTES_DATA_SIZE) {
            Log.i(TAG, "sendBytePayload: ERROR failed to send payload! payload to large: " + payload.length + "/" + ConnectionsClient.MAX_BYTES_DATA_SIZE);
            return;
        }

        if (!connection.getState().equals(ConnectionState.CONNECTED)) {
            Log.i(TAG, "sendBytePayload: ERROR failed to send payload! the intended target connection is not actually connected to us: '" + connection.getId() + "'");
            return;
        }

        if (connectionLinkedHashMap.containsValue(connection)) {
            Log.i(TAG, "sendBytePayload: sent byte payload to: " + connection.getId());
            connectionsClient.sendPayload(connection.getId(), Payload.fromBytes(payload));
        } else {
            Log.i(TAG, "sendBytePayload: ERROR unknown connection(id:" + connection.getId() + "), " +
                    "failed to send payload(" + Arrays.toString(payload) + "). Has the " +
                    "maximum connections limit(" + connectionLinkedHashMap.size() + "/" +
                    MAX_NMB_CONNECTIONS + ") been reached?");
        }
    }

    /**
     * Method used to send a byte[] payload to all connected endpoints
     *
     * @param payload byte[] with the content to send
     */
    @Override
    public void broadcastBytePayload(byte[] payload) {
        if (payload != null) {
            Log.i(TAG, "broadcastBytePayload: broadcasting payload: " + Arrays.toString(payload));
            for (Connection connection : connectionLinkedHashMap.values()) {
                if (connection.getState().equals(ConnectionState.CONNECTED)) {
                    sendBytePayload(connection, payload);
                }
            }
        } else {
            Log.i(TAG, "broadcastBytePayload: ERROR received null payload");
        }
    }

    /**
     * A getter for isScanning
     *
     * @return boolean isScanning
     */
    @Override
    public boolean isScanning() {
        return isScanning;
    }

    /**
     * A getter for isHostBeaconActive
     *
     * @return boolean isHostBeaconActive
     */
    @Override
    public boolean isHostBeaconActive() {
        return isHostBeaconActive;
    }

    /**
     * A getter for isHost
     *
     * @return boolean isHost
     */
    @Override
    public boolean isHost() {
        return isHost;
    }

    /**
     * Method used to determine if a given id string is equal to the local endpoint id
     *
     * @param id String endpointId
     * @return boolean string is equal to local endpoint id
     */
    @Override
    public boolean isMe(String id) {
        if (playerId == null) {
            Log.i(TAG, "isMe: ERROR attempt to check if ID is mine, when my ID has not been set");
            return false;
        }
        return playerId.equals(id);
    }

    /**
     * Method to disconnect with every connected endpoint and also stops endpoint scanning and
     * the hosting beacon.
     */
    @Override
    public void stopAllConnections() { //todo implement a 'reset'-method and let this method leave ad/dis state alone?
        Log.i(TAG, "stopAllConnections: terminated all connections");
        for (Connection connection : connectionLinkedHashMap.values()) {
            disconnect(connection);
        }
        connectionsClient.stopAllEndpoints(); //Ensure all are stopped
        if (isScanning) {
            stopScan();
        }
        if (isHostBeaconActive) {
            stopHostBeacon();
        }
        isHost = false;
    }

    /**
     * Method used to clean up and release network resources. Intended to be called upon
     * when resetting the networkModule as well as when the service is no longer needed
     */
    @Override
    public void cleanStop() {
        if (networkModule == null) {
            Log.i(TAG, "cleanStop: ERROR already performed a cleanStop");
        } else {
            Log.i(TAG, "cleanStop: performing a clean stop");
            connectionsClient.stopDiscovery();
            isScanning = false;
            connectionsClient.stopAdvertising();
            isHostBeaconActive = false;
            stopAllConnections();
            payloadQueueList.clear();
            isQueuingIncomingPayloads = false;
            connectionLinkedHashMap.clear();
            connectionsClient = null; //Expect null pointer exceptions if continued use of module
            networkModule = null; //Clear own instance reference
        }
    }

    /**
     * Get the number of discovered endpoints
     *
     * @return int number of discovered endpoints
     */
    @Override
    public int getConnectionsCount() {
        return connectionLinkedHashMap.size();
    }

    /**
     * Method used by clients to retrieve the connection of the host
     *
     * @return Connection the connection of the host
     */
    @Override
    public Connection getConnectionHost() {
        if (!isHost) {
            for (Connection c : connectionLinkedHashMap.values()) {
                if (c.getType().equals(ConnectionType.PEER) && c.getState().equals(ConnectionState.CONNECTED)) {
                    return c;
                }
            }
            Log.i(TAG, "getConnectionHost: ERROR attempt to get connected host, but there is no host connected - returning null");
        } else {
            Log.i(TAG, "getConnectionHost: ERROR attempt to get connected host, but I am the host - returning null");
        }
        return null;
    }

    /**
     * Method used to retrieve the connection of a given endpoint id
     *
     * @param id String endpointId
     * @return Connection of endpoint
     */
    @Override
    public Connection getConnection(String id) {
        return connectionLinkedHashMap.get(id);
    }

    /**
     * Method used to retrieve all of the discovered connections
     *
     * @return Connection[] all discovered connections
     */
    @Override
    public Connection[] getConnections() {
        Collection<Connection> connections = connectionLinkedHashMap.values();
        return connectionLinkedHashMap.values().toArray(new Connection[connections.size()]);
    }

    /**
     * Method used to disconnect from a endpoint with the given endpoint id
     *
     * @param id String endpoint id of the connection to disconnect from.
     */
    @Override
    public void disconnect(String id) {
        Log.i(TAG, "disconnect: disconnecting: " + id);
        Connection connection = connectionLinkedHashMap.get(id);
        if (connection != null) {
            if (connection.getState().equals(ConnectionState.CONNECTING)) {
                //Update stored state
                ConnectionState oldState = connection.getState();
                connection.setState(ConnectionState.DISCONNECTING); //Set to disconnecting, as connection will happen anyways in background, but will immediately be dropped when state is detected as disconnecting

                //Perform call back
                networkCallback.onConnectionChanged(connection, oldState, connection.getState());
                networkCallback.onConnectionLost(id);
            } else if (connection.getState().equals(ConnectionState.CONNECTED)) {
                connectionsClient.disconnectFromEndpoint(connection.getId());

                //Update stored state
                ConnectionState oldState = connection.getState();
                connection.setState(ConnectionState.DISCONNECTED); //Set directly to disconnected, as no callback is given to local player upon manual disconnection

                //Perform call back
                networkCallback.onConnectionChanged(connection, oldState, connection.getState());
                networkCallback.onConnectionLost(id);
            } else {
                Log.i(TAG, "disconnect: ERROR connection was not connected in the first place - only triggering onConnectionChanged callback");

                //Update stored state
                ConnectionState oldState = connection.getState();
                connection.setState(ConnectionState.DISCONNECTED); //Set directly to disconnected, as no callback is given to local player upon manual disconnection

                //Perform call back
                networkCallback.onConnectionChanged(connection, oldState, connection.getState());
            }
        } else {
            Log.i(TAG, "disconnect: ERROR no valid connection with given id was found");
        }

    }

    /**
     * Method used to disconnect from a given connection
     *
     * @param connection Connection to disconnect from
     */
    @Override
    public void disconnect(Connection connection) {
        if (connection != null) {
            disconnect(connection.getId());
        } else {
            Log.i(TAG, "disconnect: ERROR invalid connection, null connection reference");
        }
    }

    /**
     * Getter for the local connection name
     *
     * @return String local connection name
     */
    @Override
    public String getMyConnectionName() {
        return connectionName;
    }

    /**
     * Setter for the local connection name
     *
     * @param name String new local connection name
     */
    @Override
    public void setMyConnectionName(String name) {
        if (name == null) {
            Log.i(TAG, "setMyConnectionName: ERROR invalid playername, null string: '" + name + "'");
            return;
        }

        if (name.equals("")) {
            Log.i(TAG, "setMyConnectionName: ERROR invalid playername, empty string: '" + name + "'");
            return;
        }

        if (name.contains("\\")) {
            Log.i(TAG, "setMyConnectionName: ERROR invalid playername, contains illegal characters: '" + name + "'");
            return;
        }

        if (name.length() > MAX_PLAYERNAME_LENGTH) {
            Log.i(TAG, "setMyConnectionName: ERROR invalid playername, string too long: '" + name + "'");
            return;
        }

        Log.i(TAG, "setMyConnectionName: setting playername: '" + name + "'");
        connectionName = name;
    }

    /**
     * Getter of the local connection id
     *
     * @return String local connection id
     */
    @Override
    public String getMyConnectionId() {
        return playerId;
    }

    /**
     * Setter for the local connection id
     *
     * @param id String local connection id
     */
    @Override
    public void setMyConnectionId(String id) {
        Log.i(TAG, "setMyConnectionId: : setting playerId: '" + id + "'");
        this.playerId = id;
    }

    /**
     * Getter for isQueuingIncomingPayloads
     *
     * @return boolean value of isQueuingIncomingPayloads
     */
    @Override
    public boolean isQueuingIncomingPayloads() {
        return isQueuingIncomingPayloads;
    }

    /**
     * Setter for isQueuingIncomingPayloads
     *
     * @param doQueue boolean value to set isQueuingIncomingPayloads to
     */
    @Override
    public void setQueueIncomingPayloads(boolean doQueue) {
        isQueuingIncomingPayloads = doQueue;
    }

}

