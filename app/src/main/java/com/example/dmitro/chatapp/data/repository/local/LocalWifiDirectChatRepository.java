package com.example.dmitro.chatapp.data.repository.local;

import com.example.dmitro.chatapp.data.model.wifiDirect.request.Request;
import com.example.dmitro.chatapp.data.repository.ChatDataSource;
import com.example.dmitro.chatapp.utils.event.Event0;
import com.example.dmitro.chatapp.utils.event.Event1;

import java.util.List;

/**
 * Created by dmitro on 11.10.17.
 */

public class LocalWifiDirectChatRepository implements ChatDataSource {


    private static LocalWifiDirectChatRepository instance;

    private LocalWifiDirectChatRepository() {
    }

    public static LocalWifiDirectChatRepository getInstance() {
        if (instance == null) instance = new LocalWifiDirectChatRepository();
        return instance;
    }




    @Override
    public void getMessages(String key, Event1<List<com.example.dmitro.chatapp.data.model.wifiDirect.Message>> messages, Event1<String> failure, Event0 complate) {
//        Realm realm = Realm.getDefaultInstance();
//        RealmResults<com.example.dmitro.chatapp.data.model.wifiDirect.Message> allMessages = realm.where(com.example.dmitro.chatapp.data.model.wifiDirect.Message.class).findAllAsync();
//        allMessages.addChangeListener(msg -> {
//            List<com.example.dmitro.chatapp.data.model.wifiDirect.Message> arr = realm.copyFromRealm(msg);
//            messages.call(arr);
//
//        });
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
