package com.example.dmitro.chatapp.screen.setting.wifi_direct.searchServer;

import android.content.Intent;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.dmitro.chatapp.R;
import com.example.dmitro.chatapp.connection.sockets.ClientService;
import com.example.dmitro.chatapp.screen.chat.tcp_ip.TCPChatActivity;
import com.example.dmitro.chatapp.screen.setting.wifi_direct.PeersWifiDirectActivity;
import com.example.dmitro.chatapp.screen.setting.wifi_direct.other.PeerRecyclerAdapter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.dmitro.chatapp.ChatApp.EXTRAS_GROUP_OWNER_ADDRESS;
import static com.example.dmitro.chatapp.ChatApp.EXTRAS_GROUP_OWNER_PORT;
import static com.example.dmitro.chatapp.ChatApp.REQUEST_CODE_FOR_CHAT;

/**
 * Created by dmitro on 10.10.17.
 */

public class ConnectionToServerFragment extends Fragment implements ConnectionToServerContract.View, WifiP2pManager.PeerListListener, WifiP2pManager.ActionListener, WifiP2pManager.ConnectionInfoListener {
    private ConnectionToServerContract.Presenter presenter;


    @BindView(R.id.peersRV)
    public RecyclerView recyclerView;


    private PeerRecyclerAdapter peerRecyclerAdapter;


    private HashSet<String> hostAddress;

    public static ConnectionToServerFragment getInstance() {
        return new ConnectionToServerFragment();
    }

    public ConnectionToServerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wifi_direct_search_server, container, false);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        peerRecyclerAdapter = new PeerRecyclerAdapter(new ArrayList<>(), this::connectToDevice);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(peerRecyclerAdapter);

        hostAddress = new HashSet<>();

    }


    @Override
    public void initPresenter(ConnectionToServerContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void connectionToServer(String ip, int port) {

    }

    @Override
    public void showMessage(int id) {

    }

    @Override
    public void onPeersAvailable(WifiP2pDeviceList wifiP2pDeviceList) {
        ArrayList<WifiP2pDevice> list = new ArrayList(wifiP2pDeviceList.getDeviceList());

        peerRecyclerAdapter.updateData(list);

//        Toast.makeText(getContext(), "new peers", Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onConnectionInfoAvailable(WifiP2pInfo info) {


        if (info.groupFormed && info.isGroupOwner) {
////                Intent intent = new Intent(this, ServerService.class);
////                startService(intent);
////                MyUtils.WIFIDirect.setServerType();
////                Intent chatIntent = new Intent(this, TCPChatActivity.class);
////                startActivity(chatIntent);
//
//
        } else if (info.groupFormed) {

            hostAddress.add(info.groupOwnerAddress.getHostAddress());

    //        if (hostAddress.contains(info.groupOwnerAddress.getHostAddress())) {
                Intent intent = new Intent(getContext(), ClientService.class);
                intent.putExtra(EXTRAS_GROUP_OWNER_ADDRESS,
                        info.groupOwnerAddress.getHostAddress());
                intent.putExtra(EXTRAS_GROUP_OWNER_PORT, Integer.valueOf(getString(R.string.default_port)));
                getContext().startService(intent);

                Intent chatIntent = new Intent(getContext(), TCPChatActivity.class);
                getActivity().startActivityForResult(chatIntent, REQUEST_CODE_FOR_CHAT);
            }
        }
 //   }

    @Override
    public void onSuccess() {
//        Toast.makeText(getContext(), "WIFI DIRECT ENABLED", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onFailure(int i) {
//        Toast.makeText(getContext(), "WIFI DIRECT DISABLED", Toast.LENGTH_SHORT).show();

    }

    private void connectToDevice(WifiP2pDevice device) {
        WifiP2pConfig config = new WifiP2pConfig();
        config.deviceAddress = device.deviceAddress;
        ((PeersWifiDirectActivity) getActivity()).manager.connect(((PeersWifiDirectActivity) getActivity()).channel, config, new WifiP2pManager.ActionListener() {

            @Override
            public void onSuccess() {
                Toast.makeText(getContext(), "Connected", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(int reason) {
//                Toast.makeText(getContext(), "FAilure connected", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
