package com.example.dmitro.chatapp.test;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Iterator;

/**
 * Created by dmitro on 09.10.17.
 */

public class BRDirect extends BroadcastReceiver {
    private WifiP2pManager manager;
    private WifiP2pManager.Channel channel;
    private MainActivity activity;

    public static final String TAG = "log_receiver";

    public BRDirect(WifiP2pManager mManager, WifiP2pManager.Channel mChannel, MainActivity mainActivity) {
        super();
        this.manager = mManager;
        this.channel = mChannel;
        this.activity = mainActivity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {

            // UI update to indicate wifi p2p status.
                int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
                if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                    // Wifi Direct mode is enabled
                    Log.d(TAG, "Wifi Direct mode is enabled: ");
                } else {
                    Log.d(TAG, "Wifi Direct mode is disbled: ");


                }
            Log.d(TAG, "P2P state changed - " + state);
        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {

            // request available peers from the wifi p2p manager. This is an
            // asynchronous call and the calling activity is notified with a
            // callback on PeerListListener.onPeersAvailable()
            //  if (manager != null) {
            manager.requestPeers(channel, new WifiP2pManager.PeerListListener() {
                @Override
                public void onPeersAvailable(WifiP2pDeviceList wifiP2pDeviceList) {
                    Collection<WifiP2pDevice> device = wifiP2pDeviceList.getDeviceList();
                    Log.d(TAG, "P2P peers changed : " + wifiP2pDeviceList.getDeviceList().size());
                    Iterator<WifiP2pDevice> i = wifiP2pDeviceList.getDeviceList().iterator();
                    WifiP2pDevice devi = null;
                    while (i.hasNext()) {
                        devi = i.next();
                        Log.d(TAG, "details pir address: " + devi.deviceAddress);
                        Log.d(TAG, "details pir desc: " + devi.toString());
                    }
                    //////connect///////////////

                    WifiP2pConfig config = new WifiP2pConfig();
                    config.deviceAddress = devi.deviceAddress;
                    manager.connect(channel, config, new WifiP2pManager.ActionListener() {

                        @Override
                        public void onSuccess() {
                            Log.d(TAG, "connected");




                        }

                        @Override
                        public void onFailure(int reason) {
                            Log.d(TAG, "failure");
                        }
                    });


                    /////////////
                }
            });
            // }
            Log.d(TAG, "P2P peers changed");
        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
            Log.d(TAG, "device status changed");

            if (manager == null) {
                return;
            }

            NetworkInfo networkInfo = (NetworkInfo) intent
                    .getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);

            if (networkInfo.isConnected()) {

                // we are connected with the other device, request connection
                // info to find group owner IP


                manager.requestConnectionInfo(channel, new WifiP2pManager.ConnectionInfoListener() {
                    @Override
                    public void onConnectionInfoAvailable(WifiP2pInfo wifiP2pInfo) {
                        Log.d(TAG, "// ми пов'язані з іншим пристроєм, запитуємо з'єднання\n" +
                                "                 // інформація, щоб знайти IP-адресу власника групи: ");
                    }
                });
            } else {
                // It's a disconnect
                Log.d(TAG, "disconect: ");
            }
        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
//            DeviceListFragment fragment = (DeviceListFragment) activity.getFragmentManager()
//                    .findFragmentById(R.id.frag_list);
//            fragment.updateThisDevice((WifiP2pDevice) intent.getParcelableExtra(
//                    WifiP2pManager.EXTRA_WIFI_P2P_DEVICE));

            // }
        }
    }

    public boolean copyText(InputStream inputStream, OutputStream out) {
        byte buf[] = new byte[1024];
        int len;
        try {
            while ((len = inputStream.read(buf)) != -1) {
                out.write(buf, 0, len);

            }
            out.close();
            inputStream.close();
        } catch (IOException e) {
            Log.d(TAG, e.toString());
            return false;
        }
        return true;
    }

}
