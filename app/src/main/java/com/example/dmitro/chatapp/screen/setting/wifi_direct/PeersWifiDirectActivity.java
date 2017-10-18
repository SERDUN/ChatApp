package com.example.dmitro.chatapp.screen.setting.wifi_direct;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.Toast;

import com.example.dmitro.chatapp.DetectActivity;
import com.example.dmitro.chatapp.R;
import com.example.dmitro.chatapp.screen.setting.tcp_ip.ViewPagerAdapter;
import com.example.dmitro.chatapp.screen.setting.wifi_direct.create_server.CreateServerFragment;
import com.example.dmitro.chatapp.screen.setting.wifi_direct.create_server.CreateServerPresenter;
import com.example.dmitro.chatapp.screen.setting.wifi_direct.other.WiFiDirectBroadcastReceiver;
import com.example.dmitro.chatapp.screen.setting.wifi_direct.searchServer.ConnectionToServerFragment;
import com.example.dmitro.chatapp.screen.setting.wifi_direct.searchServer.ConnectionToServerPresenter;

import java.util.Collection;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.dmitro.chatapp.ChatApp.REQUEST_CODE_FOR_CHAT;

public class PeersWifiDirectActivity extends DetectActivity implements PeersWifiDirectContract.View,WifiP2pManager.ChannelListener {


    public static final String TAG = "WifiDirectActivity_log";


    @BindView(R.id.tabs)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewPager;

    private PeersWifiDirectContract.Presenter presenter;

    public WifiP2pManager manager;
    public WifiP2pManager.Channel channel;
    public WiFiDirectBroadcastReceiver receiver;

    private IntentFilter intentFilter;


    private ConnectionToServerFragment connectionToServerFragment;
    private CreateServerFragment createServerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pears_wifi_direct);
        ButterKnife.bind(this);
        new PeersWifiDirectPresenter(this);
        init();
        initWifiManager();
        initIntentFilter();
        initListener();
    }

    private void initWifiManager() {
        manager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        channel = manager.initialize(this, getMainLooper(), new WifiP2pManager.ChannelListener() {
            @Override
            public void onChannelDisconnected() {
                Log.d(TAG, "onChannelDisconnected: ");
            }
        });
    }

    private void init() {

        tabLayout.setupWithViewPager(viewPager);
        createServerFragment = CreateServerFragment.getInstance();
        connectionToServerFragment = ConnectionToServerFragment.getInstance();

        connectionToServerFragment = ConnectionToServerFragment.getInstance();
        createServerFragment = CreateServerFragment.getInstance();

        setupViewPager(viewPager);

    }

    private void setupViewPager(ViewPager viewPager) {
        new CreateServerPresenter(createServerFragment);
        new ConnectionToServerPresenter(connectionToServerFragment);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(connectionToServerFragment, getString(R.string.connect_to_server));
        adapter.addFragment(createServerFragment, getString(R.string.create_server));

        viewPager.setAdapter(adapter);
    }

    private void initListener() {

        manager.discoverPeers(channel, connectionToServerFragment);
        receiver = new WiFiDirectBroadcastReceiver(manager, channel, this, connectionToServerFragment, connectionToServerFragment);
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




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_FOR_CHAT) {
           // closeConnection();
        } else {
            createServerFragment.onActivityResult(requestCode, resultCode, data);
        //    connectionToServerFragment.onActivityResult(requestCode, resultCode, data);
        }


    }


    public void closeConnection() {

      ///  manager.removeGroup(channel,null);
        // manager.initialize(this, getMainLooper(), this);



//
//        manager.removeGroup(channel, new WifiP2pManager.ActionListener() {
//            @Override
//            public void onSuccess() {
//
////                channel = manager.initialize(getBaseContext(), getMainLooper(), null);
////                manager.discoverPeers(channel, connectionToServerFragment);
//
//            }
//            @Override
//            public void onFailure(int reason) {
//            }
//        });



//        try {
//            Method[] methods = WifiP2pManager.class.getMethods();
//            for (int i = 0; i < methods.length; i++) {
//                if (methods[i].getName().equals("deletePersistentGroup")) {
//                    // Delete any persistent group
//                    for (int netid = 0; netid < 32; netid++) {
//                        methods[i].invoke(manager, channel, netid, null);
//                    }
//                }
//            }
//        } catch(Exception e) {
//            e.printStackTrace();
//        }



//        manager.removeGroup(channel, new WifiP2pManager.ActionListener() {
//            @Override
//            public void onSuccess() {
//
//            }
//
//            @Override
//            public void onFailure(int i) {
//
//            }
//        });
    }

    @Override
    public void onChannelDisconnected() {
        if (manager != null ) {
            Toast.makeText(this, "Channel lost. Trying again", Toast.LENGTH_LONG).show();
            manager.initialize(this, getMainLooper(), this);
        }
        else {
            Toast.makeText(this,
                    "Severe! Channel is probably lost premanently. Try Disable/Re-Enable P2P.",
                    Toast.LENGTH_LONG).show();
        }

        registerReceiver(receiver, intentFilter);

    }
}
