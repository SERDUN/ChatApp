package com.example.dmitro.chatapp.screen.chat.tcp_ip;

import com.example.dmitro.chatapp.data.model.wifiDirect.Action;
import com.example.dmitro.chatapp.data.model.wifiDirect.request.Request;
import com.example.dmitro.chatapp.data.repository.managers.WifiDirectChatRepositoryManager;
import com.example.dmitro.chatapp.utils.MyUtils;

/**
 * Created by dmitro on 11.10.17.
 */

public class TCPChatWifiDirectPresenter implements TCPChatContract.Presenter {
    private TCPChatContract.View view;
    private WifiDirectChatRepositoryManager wifiDirectChatRepositoryManager;

    private String LOG = "log_observer";

    public TCPChatWifiDirectPresenter(TCPChatContract.View view, WifiDirectChatRepositoryManager wifiDirectChatRepositoryManager) {
        this.view = view;
        this.wifiDirectChatRepositoryManager = wifiDirectChatRepositoryManager;
        view.initPresenter(this);
        init();
    }

    private void init() {


    }

    @Override
    public void getMessages() {

    }

    @Override
    public void disconnect() {
        view.sendMessage(new Request(Action.DISCONNECT));
    }

    @Override
    public void sendMessage(String msg) {
        view.sendMessage(new Request(MyUtils.WIFIDirect.getCurrentUser(), msg, System.currentTimeMillis()));
    }
}
