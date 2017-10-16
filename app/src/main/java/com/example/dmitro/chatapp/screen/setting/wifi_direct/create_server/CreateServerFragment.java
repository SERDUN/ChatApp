package com.example.dmitro.chatapp.screen.setting.wifi_direct.create_server;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.dmitro.chatapp.R;
import com.example.dmitro.chatapp.connection.sockets.SocketsManager;
import com.example.dmitro.chatapp.data.model.wifiDirect.User;
import com.example.dmitro.chatapp.screen.chat.host.HostChatActivity;
import com.example.dmitro.chatapp.screen.chat.tcp_ip.TCPChatActivity;
import com.example.dmitro.chatapp.utils.Observer;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.dmitro.chatapp.ChatApp.BROADCAST_CONNECT_ACTION;
import static com.example.dmitro.chatapp.ChatApp.EXTRAS_CONNECT;
import static com.example.dmitro.chatapp.ChatApp.STATUS_SERVER_STARTED_FAILURE;
import static com.example.dmitro.chatapp.ChatApp.STATUS_SERVER_STARTED_SUCCESS;

/**
 * Created by dmitro on 10.10.17.
 */

public class CreateServerFragment extends Fragment implements CreateServerContract.View {

    private CreateServerContract.Presenter presenter;


    private ConnectedUsersRecyclerAdapter connectedUsersRecyclerAdapter;

    BroadcastReceiver broadcastReceiver;

    @BindView(R.id.startServerBT)
    Button startServerBT;

    @BindView(R.id.dialoguePageBT)
    Button dialoguePageButton;

    @BindView(R.id.connectedPeersRV)
    RecyclerView recyclerView;


    public static CreateServerFragment getInstance() {
        return new CreateServerFragment();
    }

    public CreateServerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wifi_server, container, false);
        ButterKnife.bind(this, view);
        initView();
        initListener();
        return view;
    }

    private void initListener() {

        SocketsManager.getInstance(getContext().getContentResolver()).registerObserver(new Observer() {
            @Override
            public <ArrayList> void update(ArrayList t) {
                getActivity().runOnUiThread(() -> {
                    connectedUsersRecyclerAdapter.updateData((java.util.ArrayList<User>) t);
                });
            }
        });

        broadcastReceiver=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int status = intent.getIntExtra(EXTRAS_CONNECT, 0);

                switch (status) {
                    case STATUS_SERVER_STARTED_SUCCESS:
                        dialoguePageButton.setEnabled(true);
                        startServerBT.setEnabled(false);
                        startServerBT.setText("server created");
                        break;
                    case STATUS_SERVER_STARTED_FAILURE:
                        dialoguePageButton.setEnabled(false);
                        Toast.makeText(getContext(), "SERVER FAILURE", Toast.LENGTH_SHORT).show();
                        break;

                }
            }
        };
        IntentFilter intFilt = new IntentFilter(BROADCAST_CONNECT_ACTION);
        // регистрируем (включаем) BroadcastReceiver
        getContext().registerReceiver(broadcastReceiver, intFilt);
    }

    private void initView() {
        startServerBT.setOnClickListener(view -> presenter.startServer());
        dialoguePageButton.setOnClickListener(view -> presenter.startDialogue());
        connectedUsersRecyclerAdapter = new ConnectedUsersRecyclerAdapter(new ArrayList<>(), null);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(connectedUsersRecyclerAdapter);


    }


    @Override
    public void initPresenter(CreateServerContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void startServer() {

    }

    @Override
    public void showMessage(int id) {

    }

    @Override
    public void showDialogueActivity() {
        startActivity(new Intent(getContext(), HostChatActivity.class));

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
       getContext().unregisterReceiver(broadcastReceiver);

    }
}
