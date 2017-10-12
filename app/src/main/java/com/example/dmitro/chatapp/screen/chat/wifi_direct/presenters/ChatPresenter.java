package com.example.dmitro.chatapp.screen.chat.wifi_direct.presenters;

import android.content.SharedPreferences;

import com.example.dmitro.chatapp.ChatApp;
import com.example.dmitro.chatapp.R;
import com.example.dmitro.chatapp.data.model.firebase.User;
import com.example.dmitro.chatapp.data.model.wifiDirect.Message;
import com.example.dmitro.chatapp.data.repository.managers.ChatRepositoryManager;
import com.example.dmitro.chatapp.screen.chat.wifi_direct.ChatContract;

/**
 * Created by dmitro on 08.10.17.
 */

public class ChatPresenter implements ChatContract.Presenter {
    private ChatContract.View view;
    private ChatRepositoryManager chatRepositoryManager;
    private String keyChannel;
    private User user;
    private SharedPreferences sharedPreferences;

    public ChatPresenter(ChatContract.View view, ChatRepositoryManager chatRepositoryManager, SharedPreferences sharedPreferences, String key) {
        this.view = view;
        this.chatRepositoryManager = chatRepositoryManager;
        this.sharedPreferences = sharedPreferences;
        this.keyChannel = key;
        view.initPresenter(this);
        user = new User(sharedPreferences.getString(ChatApp.getInstance().getString(R.string.user_name), "error"), "man");
    }

    @Override
    public void getMessages() {
        chatRepositoryManager.getMessages(keyChannel, s -> {
            view.showMessages(s);
        }, f -> {
        }, () -> {
        });
    }

    @Override
    public void sendMessage(String message) {
        chatRepositoryManager.sendMessage(keyChannel, new Message( user.getLogin(), message,System.currentTimeMillis()), s -> {
            view.showMessages(s);
        }, f -> {
        }, () -> {
        });
        getMessages();
    }
}
