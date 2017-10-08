package com.example.dmitro.chatapp.data.repository;

import android.content.Context;

import com.example.dmitro.chatapp.ChatApp;
import com.example.dmitro.chatapp.R;
import com.example.dmitro.chatapp.connection.TypeConnection;
import com.example.dmitro.chatapp.data.repository.local.LocalFirebaseChatRepository;
import com.example.dmitro.chatapp.data.repository.remote.firebase.RemoteFirebaseChatRepository;
import com.example.dmitro.chatapp.utils.MyUtils;

/**
 * Created by dmitro on 08.10.17.
 */

public class Injection {
    public static ChatRepositoryManager provideManager() {
        TypeConnection typeConnection = MyUtils.getCurrentTypeCoonection();

        switch (typeConnection) {
            case CONNECT_TO_SERVER_FIREBASE:
                return (ChatRepositoryManager) ChatRepositoryManager.getInstance(LocalFirebaseChatRepository.getInstance(),
                        RemoteFirebaseChatRepository.getInstance());
            case CONNECT_TO_BLUETOOTH:
                break;
            case CONNECT_TO_SERVER_TOMCAT:
                break;
            case CONNECT_TO_WIFI_DIRECT:
                break;
        }
        return null;
    }

}
