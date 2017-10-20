package com.example.dmitro.chatapp.screen.setting.tcp_ip.search_server;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.dmitro.chatapp.R;
import com.example.dmitro.chatapp.connection.sockets.ClientService;
import com.example.dmitro.chatapp.screen.ChatConst;
import com.example.dmitro.chatapp.screen.chat.client.ClientChatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.dmitro.chatapp.ChatApp.EXTRAS_GROUP_OWNER_ADDRESS;
import static com.example.dmitro.chatapp.ChatApp.EXTRAS_GROUP_OWNER_PORT;

/**
 * Created by dmitro on 10.10.17.
 */

public class ConnectionToServerFragment extends Fragment implements ConnectionToServerContract.View {
    ConnectionToServerContract.Presenter presenter;

    @BindView(R.id.ipConnectingServer)
    public TextView ipConnectingServer;

    @BindView(R.id.portServersEditText)
    public EditText portServerET;

    @BindView(R.id.connectToServerBT)
    public Button connectToServerButton;

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
        View view = inflater.inflate(R.layout.fragment_connection_to_server, container, false);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        ipConnectingServer.setText(presenter.getWifiNetworkIP());
        portServerET.setText(getResources().getText(R.string.default_port));
        connectToServerButton.setOnClickListener(view -> {
            startActivity(createIntentForService());
        });
    }

    private Intent createIntentForService() {
        Intent intent=new Intent(getContext(), ClientChatActivity.class);
        intent.putExtra(EXTRAS_GROUP_OWNER_ADDRESS,ipConnectingServer.getText().toString());
        intent.putExtra(EXTRAS_GROUP_OWNER_PORT, Integer.valueOf(portServerET.getText().toString()));
        intent.putExtra(ChatConst.IS_HOST_SERVICE, false);


        return intent;
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
}
