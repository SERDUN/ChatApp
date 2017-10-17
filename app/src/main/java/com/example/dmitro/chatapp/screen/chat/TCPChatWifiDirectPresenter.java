package com.example.dmitro.chatapp.screen.chat;

import com.example.dmitro.chatapp.data.model.wifiDirect.socket.data_object.Body;
import com.example.dmitro.chatapp.data.model.wifiDirect.socket.data_object.Type;
import com.example.dmitro.chatapp.utils.MyUtils;

/**
 * Created by dmitro on 11.10.17.
 */

public class TCPChatWifiDirectPresenter implements TCPChatContract.Presenter {
    private TCPChatContract.View view;

    private String LOG = "log_observer";

    public TCPChatWifiDirectPresenter(TCPChatContract.View view) {
        this.view = view;
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
    public void sendMessage(byte[] msg, Type type) {
        Body body = new Body(MyUtils.WIFIDirect.getCurrentUser().getLogin(), System.currentTimeMillis(), msg, type);
        view.sendMessage(body);
    }


}
