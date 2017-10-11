package com.example.dmitro.chatapp.data.repository.managers;

import com.example.dmitro.chatapp.data.model.firebase.ChannelKey;
import com.example.dmitro.chatapp.data.model.wifiDirect.Message;
import com.example.dmitro.chatapp.data.model.wifiDirect.Request;
import com.example.dmitro.chatapp.data.repository.ChatDataSource;
import com.example.dmitro.chatapp.utils.event.Event0;
import com.example.dmitro.chatapp.utils.event.Event1;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by dmitro on 11.10.17.
 */

public class WifiDirectChatRepositoryManager implements ChatDataSource, Observable {
    private static WifiDirectChatRepositoryManager instance = null;

    private final ChatDataSource chatLocalRepository;
    private final ChatDataSource chatRemoteRepository;


    private ArrayList<Observer> observers;


    private WifiDirectChatRepositoryManager(ChatDataSource chatLocalRepository, ChatDataSource chatRemoteRepository) {
        this.chatLocalRepository = chatLocalRepository;
        this.chatRemoteRepository = chatRemoteRepository;
        observers = new ArrayList<>();
    }

    public static ChatDataSource getInstance(ChatDataSource local, ChatDataSource remote) {
        if (instance == null) {
            instance = new WifiDirectChatRepositoryManager(local, remote);
            return instance;
        }

        return instance;
    }

    @Override
    public void getListChannel(Event1<List<ChannelKey>> channels, Event1<String> failure, Event0 complate) {
    }


    @Override
    public void getMessages(String key, Event1<List<Message>> messages, Event1<String> failure, Event0 complate) {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Message> results = realm.where(Message.class).findAll();
        List<Message> arr = realm.copyFromRealm(results);
        messages.call(arr);
        int d = 4;
    }

    @Override
    public void sendMessage(String key, com.example.dmitro.chatapp.data.model.wifiDirect.Message message, Event1<List<com.example.dmitro.chatapp.data.model.wifiDirect.Message>> s, Event1<String> f, Event0 c) {

    }


    @Override
    public void getLastMessage(Event1<Request> s, Event1<String> f, Event0 c) {

    }

    @Override
    public void addMessageInDatabase(Message message, Event1<com.example.dmitro.chatapp.data.model.wifiDirect.Message> s, Event1<String> f, Event0 c) {
        Realm.getDefaultInstance().executeTransaction(realm -> {
            realm.copyToRealm(message);
            notifyObserver(message);
        });
    }


    @Override
    public void registerObserver(Observer o) {
        observers.add(o);

    }

    @Override
    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    @Override
    public <T> void notifyObserver(T t) {
        for (Observer observer : observers) {
            observer.update();
        }


    }
}
