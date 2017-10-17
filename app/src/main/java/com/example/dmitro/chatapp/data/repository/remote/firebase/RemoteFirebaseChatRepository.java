package com.example.dmitro.chatapp.data.repository.remote.firebase;

import com.example.dmitro.chatapp.data.model.wifiDirect.request.Request;
import com.example.dmitro.chatapp.data.repository.ChatDataSource;
import com.example.dmitro.chatapp.utils.event.Event0;
import com.example.dmitro.chatapp.utils.event.Event1;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

/**
 * Created by dmitro on 08.10.17.
 */

public class RemoteFirebaseChatRepository implements ChatDataSource {
    private final String TAG = "ChatRepository_log";
    private static RemoteFirebaseChatRepository instance;
    private FirebaseDatabase firebaseDatabase;

    private RemoteFirebaseChatRepository() {
        firebaseDatabase = FirebaseDatabase.getInstance();
    }

    public static RemoteFirebaseChatRepository getInstance() {
        if (instance == null) instance = new RemoteFirebaseChatRepository();
        return instance;
    }



    @Override
    public void getMessages(String key, Event1<List<com.example.dmitro.chatapp.data.model.wifiDirect.Message>> messages, Event1<String> failure, Event0 complate) {

    }



    @Override
    public void sendMessage(String key, com.example.dmitro.chatapp.data.model.wifiDirect.Message message, Event1<List<com.example.dmitro.chatapp.data.model.wifiDirect.Message>> s, Event1<String> f, Event0 c) {

    }



    @Override
    public void getLastMessage(Event1<Request> s, Event1<String> f, Event0 c) {

    }

    @Override
    public void addMessageInDatabase(com.example.dmitro.chatapp.data.model.wifiDirect.Message message, Event1<com.example.dmitro.chatapp.data.model.wifiDirect.Message> s, Event1<String> f, Event0 c) {

    }


}
