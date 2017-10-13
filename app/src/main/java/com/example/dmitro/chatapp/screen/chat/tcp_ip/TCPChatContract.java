package com.example.dmitro.chatapp.screen.chat.tcp_ip;

import com.example.dmitro.chatapp.BasePresenter;
import com.example.dmitro.chatapp.BaseView;
import com.example.dmitro.chatapp.data.model.wifiDirect.Message;
import com.example.dmitro.chatapp.data.model.wifiDirect.Request;

import java.util.List;

/**
 * Created by dmitro on 08.10.17.
 */

public class TCPChatContract {
    public interface View extends BaseView<Presenter> {
        void showMessage(int id);

        void updateShowMessage(Request request);

        void showMessages(List<Message> FMessages);

        void sendMessage(Request request);

        void stopService();


    }

    public interface Presenter extends BasePresenter {
        public void getMessages();

        void disconnect();

        public void sendMessage(String name);

    }
}
