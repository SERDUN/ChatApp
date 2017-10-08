package com.example.dmitro.chatapp.screen.chat;

import com.example.dmitro.chatapp.BasePresenter;
import com.example.dmitro.chatapp.BaseView;
import com.example.dmitro.chatapp.data.model.firebase.Message;
import com.example.dmitro.chatapp.screen.welcome.WelcomeContract;

import java.util.List;

/**
 * Created by dmitro on 08.10.17.
 */

public class ChatContract {
    interface View extends BaseView<Presenter> {
        void showMessage(int id);
        void showMessages(List<Message> messages);
    }

    interface Presenter extends BasePresenter {
        public void getMessages();
        public void sendMessage(String name);

    }
}
