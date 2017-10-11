package com.example.dmitro.chatapp.screen.chat;

import com.example.dmitro.chatapp.BasePresenter;
import com.example.dmitro.chatapp.BaseView;
import com.example.dmitro.chatapp.data.model.wifiDirect.Message;
import com.example.dmitro.chatapp.data.model.wifiDirect.Request;

import java.util.List;

/**
 * Created by dmitro on 08.10.17.
 */

public class ChatContract {
    public interface View extends BaseView<Presenter> {
        void showMessage(int id);
        void updateShowMessage(Request request);
        void showMessages(List<Message> FMessages);

    }

    public interface Presenter extends BasePresenter {
        public void getMessages();
        public void sendMessage(String name);

    }
}
