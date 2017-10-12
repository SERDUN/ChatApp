package com.example.dmitro.chatapp.screen.setting.tcp_ip.create_server;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.dmitro.chatapp.ChatApp;
import com.example.dmitro.chatapp.R;
import com.example.dmitro.chatapp.connection.sockets.ServerService;
import com.example.dmitro.chatapp.screen.chat.tcp_ip.TCPChatActivity;
import com.example.dmitro.chatapp.utils.MyUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dmitro on 10.10.17.
 */

public class CreateServerFragment extends Fragment implements CreateServerContract.View {

    private CreateServerContract.Presenter presenter;


    @BindView(R.id.ipServerTV)
    public TextView ipServerTV;

    @BindView(R.id.portServerET)
    public EditText portServerET;

    @BindView(R.id.startServerBT)
    public Button startServerBT;


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
        View view = inflater.inflate(R.layout.fragment_create_server, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }


    private void initView() {
        ipServerTV.setText(presenter.getWifiNetworkIP());
        portServerET.setText(getResources().getText(R.string.default_port));
        startServerBT.setOnClickListener(view -> {
            MyUtils.WIFIDirect.setServerType();
            createIntentForService();
            getContext().startService(createIntentForService());
            startActivity(new Intent(getContext(), TCPChatActivity.class));
        });
    }

    private Intent createIntentForService() {
        Intent intent = new Intent(getContext(), ServerService.class);
        intent.putExtra(ChatApp.EXTRAS_GROUP_OWNER_PORT, portServerET.getText());
        return intent;
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

}
