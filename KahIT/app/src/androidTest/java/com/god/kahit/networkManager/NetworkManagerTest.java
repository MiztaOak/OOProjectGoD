package com.god.kahit.networkManager;

import android.content.Context;

import com.god.kahit.networkManager.callbacks.NetworkCallback;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.annotation.NonNull;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;


@RunWith(AndroidJUnit4.class)
public class NetworkManagerTest {

    private Context appContext;
    private NetworkManager networkManager;

    @Before
    public void before() {
        appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        networkManager = NetworkModule.getInstance(appContext, new NetworkCallback() {
            @Override
            public void onBytePayloadReceived(@NonNull String id, @NonNull byte[] receivedBytes) {

            }

//            @Override
//            public void onFilePayloadReceived(@NonNull String id, @NonNull File receivedFile) {
//
//            }

            @Override
            public void onHostFound(@NonNull String id, @NonNull Connection connection) {

            }

            @Override
            public void onHostLost(@NonNull String id) {

            }

            @Override
            public void onClientFound(@NonNull String id, @NonNull Connection connection) {

            }

            @Override
            public void onConnectionEstablished(@NonNull String id, @NonNull Connection connection) {

            }

            @Override
            public void onConnectionLost(@NonNull String id) {

            }

            @Override
            public void onConnectionChanged(@NonNull Connection connection, @NonNull ConnectionState oldState, @NonNull ConnectionState newState) {

            }
        });
    }

    @After
    public void after() {
        networkManager.cleanStop();
    }

    @Test
    public void scanAdvertiseSwitchTest() {
        networkManager.startScan();
        assertTrue(networkManager.isScanning());
        assertFalse(networkManager.isHost());
        assertFalse(networkManager.isHostBeaconActive());

        networkManager.stopScan();
        assertFalse(networkManager.isScanning());
        assertFalse(networkManager.isHost());
        assertFalse(networkManager.isHostBeaconActive());

        networkManager.startHostBeacon();
        assertFalse(networkManager.isScanning());
        assertTrue(networkManager.isHost());
        assertTrue(networkManager.isHostBeaconActive());

        networkManager.stopHostBeacon();
        assertFalse(networkManager.isScanning());
        assertTrue(networkManager.isHost());
        assertFalse(networkManager.isHostBeaconActive());

        networkManager.startScan();
        assertTrue(networkManager.isScanning());
        assertTrue(networkManager.isHost());
        assertFalse(networkManager.isHostBeaconActive());

        networkManager.stopScan();
        assertFalse(networkManager.isScanning());
        assertTrue(networkManager.isHost());
        assertFalse(networkManager.isHostBeaconActive());

        networkManager.startScan();
        assertTrue(networkManager.isScanning());
        assertTrue(networkManager.isHost());
        assertFalse(networkManager.isHostBeaconActive());

        networkManager.stopAllConnections();
        assertFalse(networkManager.isScanning());
        assertFalse(networkManager.isHost());
        assertFalse(networkManager.isHostBeaconActive());
    }

    @Test(expected = NullPointerException.class)
    public void cleanStopExceptionTest() {
        networkManager.cleanStop(); //Must get new instance after this call
        networkManager.startScan();
    }

    @Test
    public void cleanStopTest() {
        networkManager.cleanStop(); //Must get new instance after this call
        networkManager = NetworkModule.getInstance(appContext, new NetworkCallback() {
            @Override
            public void onBytePayloadReceived(@NonNull String id, @NonNull byte[] receivedBytes) {

            }

//            @Override
//            public void onFilePayloadReceived(@NonNull String id, @NonNull File receivedFile) {
//
//            }

            @Override
            public void onHostFound(@NonNull String id, @NonNull Connection connection) {

            }

            @Override
            public void onHostLost(@NonNull String id) {

            }

            @Override
            public void onClientFound(@NonNull String id, @NonNull Connection connection) {

            }

            @Override
            public void onConnectionEstablished(@NonNull String id, @NonNull Connection connection) {

            }

            @Override
            public void onConnectionLost(@NonNull String id) {

            }

            @Override
            public void onConnectionChanged(@NonNull Connection connection, @NonNull ConnectionState oldState, @NonNull ConnectionState newState) {

            }
        });
        networkManager.startScan();
    }

    @Test
    public void attemptScanAdvertiseAtSameTimeTest() {
        networkManager.startScan();
        networkManager.startHostBeacon();
        assertFalse(networkManager.isScanning());
        assertTrue(networkManager.isHost());
        assertTrue(networkManager.isHostBeaconActive());

        networkManager.startScan();
        assertTrue(networkManager.isScanning());
        assertTrue(networkManager.isHost());
        assertFalse(networkManager.isHostBeaconActive());
    }

    @Test
    public void broadcastBytePayloadTest() {
        //If crash, test fail
        networkManager.broadcastBytePayload(null);
        networkManager.broadcastBytePayload("".getBytes());
        networkManager.broadcastBytePayload("Testning123".getBytes());
    }

    @Test
    public void sendBytePayloadTest() {
        //If crash, test fail
        networkManager.sendBytePayload(null, null);
        networkManager.sendBytePayload(null, "".getBytes());
        networkManager.sendBytePayload(null, "Testning123".getBytes());
        networkManager.sendBytePayload(new Connection("1234", "TestName", ConnectionType.PEER, ConnectionState.CONNECTED), null);
        networkManager.sendBytePayload(new Connection("1234", "TestName", ConnectionType.PEER, ConnectionState.CONNECTED), "".getBytes());
        networkManager.sendBytePayload(new Connection("1234", "TestName", ConnectionType.PEER, ConnectionState.CONNECTED), "Testning123".getBytes());

        networkManager.sendBytePayload(new Connection("1234", "TestName", ConnectionType.PEER, ConnectionState.CONNECTING), null);
        networkManager.sendBytePayload(new Connection("1234", "TestName", ConnectionType.PEER, ConnectionState.CONNECTING), "".getBytes());
        networkManager.sendBytePayload(new Connection("1234", "TestName", ConnectionType.PEER, ConnectionState.CONNECTING), "Testning123".getBytes());

        networkManager.sendBytePayload(new Connection("1234", "TestName", ConnectionType.PEER, ConnectionState.DISCONNECTED), null);
        networkManager.sendBytePayload(new Connection("1234", "TestName", ConnectionType.PEER, ConnectionState.DISCONNECTED), "".getBytes());
        networkManager.sendBytePayload(new Connection("1234", "TestName", ConnectionType.PEER, ConnectionState.DISCONNECTED), "Testning123".getBytes());
    }

    @Test
    public void getConnectionTest() {
        assertNull(networkManager.getConnection(null));
        assertNull(networkManager.getConnection(""));
        assertNull(networkManager.getConnection("123465478994654asdasdv"));
        assertNull(networkManager.getConnection("    d     "));
    }

    @Test
    public void connectToNullHostTest() {
        //If crash, test fail
        networkManager.connectToHost(null);
    }

    @Test
    public void disconnectFromNullHostTest() {
        //If crash, test fail
        networkManager.disconnect("");
        networkManager.disconnect((String) null);
        networkManager.disconnect((Connection) null);
    }

    @Test
    public void playerNameGetSetTest() {
        String testNameOriginal = "Testningsnam"; //12 chars long
        networkManager.setMyConnectionName(testNameOriginal);
        assertEquals(networkManager.getMyConnectionName(), testNameOriginal);

        String testName = "Testningsnamn";  //13 chars long
        networkManager.setMyConnectionName(testName);
        assertNotEquals(networkManager.getMyConnectionName(), testName);
        assertNotEquals(networkManager.getMyConnectionName(), testName);
        assertEquals(networkManager.getMyConnectionName(), testNameOriginal);

        testName = null;
        networkManager.setMyConnectionName(testName);
        assertNotEquals(networkManager.getMyConnectionName(), testName);
        assertEquals(networkManager.getMyConnectionName(), testNameOriginal);

        testName = "";
        networkManager.setMyConnectionName(testName);
        assertNotEquals(networkManager.getMyConnectionName(), testName);
        assertEquals(networkManager.getMyConnectionName(), testNameOriginal);

        testName = "Test\\ning";
        networkManager.setMyConnectionName(testName);
        assertNotEquals(networkManager.getMyConnectionName(), testName);
        assertEquals(networkManager.getMyConnectionName(), testNameOriginal);

        testName = "12345";
        networkManager.setMyConnectionName(testName);
        assertEquals(networkManager.getMyConnectionName(), testName);
        assertNotEquals(networkManager.getMyConnectionName(), testNameOriginal);
    }
}