package com.example.dmitro.chatapp.screen.setting.wifi_direct.other;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;

import com.example.dmitro.chatapp.screen.setting.wifi_direct.PeersWifiDirectActivity;

/**
 * Created by dmitro on 09.10.17.
 */

public class WiFiDirectBroadcastReceiver extends BroadcastReceiver {
    private WifiP2pManager manager;
    private WifiP2pManager.Channel channel;
    private PeersWifiDirectActivity activity;
    private WifiP2pManager.PeerListListener peerListListener;
    private WifiP2pManager.ConnectionInfoListener connectionInfoListener;


    public static final String TAG = "log_receiver";

    public WiFiDirectBroadcastReceiver(WifiP2pManager manager, WifiP2pManager.Channel channel, PeersWifiDirectActivity activity, WifiP2pManager.PeerListListener peerListListener, WifiP2pManager.ConnectionInfoListener connectionInfoListener) {
        this.manager = manager;
        this.channel = channel;
        this.activity = activity;
        this.peerListListener = peerListListener;
        this.connectionInfoListener = connectionInfoListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getAction()) {
            case WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION:
                Log.d(TAG, " 1 onReceive: remove group");
                checkWifiDirect(intent);
                break;
            case WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION:
                Log.d(TAG, " 2 onReceive: remove group");

                changedSizePeer();

                break;
            case WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION:
                Log.d(TAG, " 3 onReceive: remove group");

                connectionChangedAction(intent);
                break;
            case WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION:
                Log.d(TAG, " 4 onReceive: remove group");

                deviceChangedAction(intent);
                break;
        }
    }


    private void checkWifiDirect(Intent intent) {
        int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
        if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
            // Wifi Direct mode is enabled
        } else {


        }
    }

    private void changedSizePeer() {
        manager.requestPeers(channel, peerListListener);
    }


    private void connectionChangedAction(Intent intent) {
        if (manager == null) {
            return;
        }

        NetworkInfo networkInfo = (NetworkInfo) intent
                .getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);

        int a = 3;
        if (networkInfo.isConnected()) {
            manager.requestConnectionInfo(channel, connectionInfoListener);
        }


    }

    private void deviceChangedAction(Intent intent) {
//        WifiP2pDevice device = intent.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_DEVICE);
//        Log.d(TAG, "Device status -" + device.status+device.deviceName);
//        switch (device.status) {
//            case WifiP2pDevice.CONNECTED:
//                Log.v(TAG,"mConnected");
//                break;
//            case WifiP2pDevice.INVITED:
//                Log.v(TAG,"mInvited");
//                break;
//            case WifiP2pDevice.FAILED:
//                Log.v(MainAct,"mFailed");
//                break;
//            case WifiP2pDevice.AVAILABLE:
//                Log.v(MainActivity.TAG,"mAvailable");
//                break;
//            case WifiP2pDevice.UNAVAILABLE:
//                Log.v(MainActivity.TAG,"mUnavailable");
//            default:
//                Log.v(MainActivity.TAG,"mUnknown");
//                break;
//        }
    }

}
