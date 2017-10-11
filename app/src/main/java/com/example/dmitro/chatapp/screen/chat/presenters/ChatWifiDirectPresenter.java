package com.example.dmitro.chatapp.screen.chat.presenters;

import android.util.Log;

import com.example.dmitro.chatapp.data.repository.managers.WifiDirectChatRepositoryManager;
import com.example.dmitro.chatapp.screen.chat.ChatContract;

/**
 * Created by dmitro on 11.10.17.
 */

public class ChatWifiDirectPresenter implements ChatContract.Presenter {
    private ChatContract.View view;
    private WifiDirectChatRepositoryManager wifiDirectChatRepositoryManager;

    private String LOG = "log_observer";

    public ChatWifiDirectPresenter(ChatContract.View view, WifiDirectChatRepositoryManager wifiDirectChatRepositoryManager) {
        this.view = view;
        this.wifiDirectChatRepositoryManager = wifiDirectChatRepositoryManager;
        init();
    }

    private void init() {
        wifiDirectChatRepositoryManager.registerObserver(() -> {
            wifiDirectChatRepositoryManager.getMessages("", s -> {
                view.showMessages(s);
            }, f -> {
            }, () -> {
            });
        });

    }

    @Override
    public void getMessages() {
        wifiDirectChatRepositoryManager.getMessages("", s -> {
            //  view.showMessages(s);
            Log.d(LOG, "getMessages: update");
        }, f -> {
        }, () -> {
        });
    }

    @Override
    public void sendMessage(String name) {

    }
}
