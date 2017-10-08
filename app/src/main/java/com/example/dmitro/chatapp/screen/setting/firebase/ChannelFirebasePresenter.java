package com.example.dmitro.chatapp.screen.setting.firebase;


import com.example.dmitro.chatapp.data.model.firebase.ChannelKey;
import com.example.dmitro.chatapp.data.repository.ChatRepositoryManager;
import com.example.dmitro.chatapp.data.repository.Injection;

import java.util.List;

/**
 * Created by dmitro on 08.10.17.
 */

public class ChannelFirebasePresenter implements ChannelFirebaseContract.Presenter {
    private ChannelFirebaseContract.View view;
    private ChatRepositoryManager chatRepositoryManager;

    public ChannelFirebasePresenter(ChannelFirebaseContract.View view, ChatRepositoryManager manager) {
        this.view = view;
        this.chatRepositoryManager = manager;
        view.initPresenter(this);
    }

    @Override
    public void getChannels() {
        chatRepositoryManager.getListChannel(s -> {
            view.showChannel(s);
        }, f -> {
        }, () -> {
        });

    }

    @Override
    public void getUsersFromTheChannel() {

    }
}

