package com.example.dmitro.chatapp.screen.chat;

import com.example.dmitro.chatapp.BasePresenter;
import com.example.dmitro.chatapp.BaseView;
import com.example.dmitro.chatapp.data.model.wifiDirect.socket.data_object.Body;
import com.example.dmitro.chatapp.data.model.wifiDirect.socket.data_object.Type;
import com.example.dmitro.chatapp.data.model.wifiDirect.socket.transport_object.Request;

import java.util.List;

/**
 * Created by dmitro on 08.10.17.
 */

public class TCPChatContract {
    public interface View extends BaseView<Presenter> {
        void showMessage(int id);

        void updateShowMessage(Request request);

        void showMessages(List<Body> FMessages);

        void sendMessage(Body message);

        void disconnect(Request request);

        void stopService();


    }

    public interface Presenter extends BasePresenter {
        public void getMessages();

        void disconnect();

        public void sendMessage(byte[] msg, Type type);

    }
}
