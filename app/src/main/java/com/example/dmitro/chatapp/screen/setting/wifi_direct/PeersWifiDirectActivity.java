package com.example.dmitro.chatapp.screen.setting.wifi_direct;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.dmitro.chatapp.R;
import com.example.dmitro.chatapp.connection.wifiDirectionHost.ClientService;
import com.example.dmitro.chatapp.connection.wifiDirectionHost.ServerService;
import com.example.dmitro.chatapp.screen.chat.ChatActivity;
import com.example.dmitro.chatapp.utils.MyUtils;

import java.util.ArrayList;
import java.util.Collection;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PeersWifiDirectActivity extends AppCompatActivity implements PeersWifiDirectContract.View {
    public static final String TAG = "WifiDirectActivity_log";
    private IntentFilter intentFilter;

    PeersWifiDirectContract.Presenter presenter;

    private WifiP2pManager manager;
    private WifiP2pManager.Channel channel;
    private WiFiDirectBroadcastReceiver receiver;

    @BindView(R.id.peersRV)
    public RecyclerView recyclerView;

    private PeerRecyclerAdapter peerRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pears_wifi_direct);
        ButterKnife.bind(this);
        new PeersWifiDirectPresenter(this);
        init();
        initIntentFilter();
        initListener();
    }

    private void init() {
        peerRecyclerAdapter = new PeerRecyclerAdapter(new ArrayList<>(), peer -> {
            connectToDevice(peer);
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        recyclerView.setAdapter(peerRecyclerAdapter);

        manager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        channel = manager.initialize(this, getMainLooper(), null);

    }

    private void initListener() {

        manager.discoverPeers(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
            }

            @Override
            public void onFailure(int reason) {


            }
        });
        WifiP2pManager.PeerListListener peerListListener = wifiP2pDeviceList -> {
            ArrayList<WifiP2pDevice> list = new ArrayList(wifiP2pDeviceList.getDeviceList());

            peerRecyclerAdapter.updateData(list);

        };

        WifiP2pManager.ConnectionInfoListener connectionInfoListener = info -> {

            if (info.groupFormed && info.isGroupOwner) {
                Intent intent = new Intent(this, ServerService.class);
                startService(intent);
                MyUtils.WIFIDirect.setServerType();
                Intent chatIntent = new Intent(this, ChatActivity.class);
                startActivity(chatIntent);

            } else if (info.groupFormed) {
                Intent intent = new Intent(this, ClientService.class);
                intent.putExtra(ClientService.EXTRAS_GROUP_OWNER_ADDRESS,
                        info.groupOwnerAddress.getHostAddress());
                intent.putExtra(ClientService.EXTRAS_GROUP_OWNER_PORT, 8988);
                startService(intent);

                Intent chatIntent = new Intent(this, ChatActivity.class);
                startActivity(chatIntent);
            }
        };


        receiver = new WiFiDirectBroadcastReceiver(manager, channel, this, peerListListener, connectionInfoListener);
        registerReceiver(receiver, intentFilter);
    }

    private void initIntentFilter() {
        intentFilter = new IntentFilter();
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_DISCOVERY_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
    }

    @Override
    public void initPresenter(PeersWifiDirectContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showPeers(Collection<WifiP2pDevice> devices) {

    }

    @Override
    public void showMessage(int id) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, intentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    private void connectToDevice(WifiP2pDevice device) {
        WifiP2pConfig config = new WifiP2pConfig();
        config.deviceAddress = device.deviceAddress;
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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: DESTROY");
        manager.removeGroup(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailure(int i) {

            }
        });




    }
}
