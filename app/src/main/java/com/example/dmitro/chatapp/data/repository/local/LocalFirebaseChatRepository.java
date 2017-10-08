package com.example.dmitro.chatapp.data.repository.local;

import com.example.dmitro.chatapp.data.model.firebase.ChannelKey;
import com.example.dmitro.chatapp.data.model.firebase.Message;
import com.example.dmitro.chatapp.data.repository.ChatDataSource;
import com.example.dmitro.chatapp.utils.event.Event0;
import com.example.dmitro.chatapp.utils.event.Event1;

import java.util.List;

/**
 * Created by dmitro on 08.10.17.
 */

public class LocalFirebaseChatRepository implements ChatDataSource {
    private static LocalFirebaseChatRepository instance;

    private LocalFirebaseChatRepository() {
    }

    public static LocalFirebaseChatRepository getInstance() {
        if (instance == null) instance = new LocalFirebaseChatRepository();
        return instance;
    }

    @Override
    public void getListChannel(Event1<List<ChannelKey>> channels, Event1<String> failure, Event0 complate) {

    }

    @Override
    public void getMessages(String key, Event1<List<Message>> channels, Event1<String> failure, Event0 complate) {

    }

    @Override
    public void sendMessage(String key, Message message, Event1<List<Message>> s, Event1<String> f, Event0 c) {

    }
}
