package com.example.dmitro.chatapp.screen.chat;

import com.example.dmitro.chatapp.data.model.wifiDirect.Message;
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
        view.stopService();
    }

    @Override
    public void sendMessage(String msg) {
        view.sendMessage(new Message(MyUtils.WIFIDirect.getCurrentUser().getLogin(), msg, System.currentTimeMillis()));
    }
}
