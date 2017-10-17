package com.example.dmitro.chatapp.data.repository.managers;

import com.example.dmitro.chatapp.data.model.wifiDirect.Message;
import com.example.dmitro.chatapp.data.model.wifiDirect.request.Request;
import com.example.dmitro.chatapp.data.repository.ChatDataSource;
import com.example.dmitro.chatapp.utils.event.Event0;
import com.example.dmitro.chatapp.utils.event.Event1;

import java.util.List;

/**
 * Created by dmitro on 08.10.17.
 */

public class ChatRepositoryManager implements ChatDataSource{
    private static ChatRepositoryManager instance = null;
    private final ChatDataSource chatLocalRepository;
    private final ChatDataSource chatRemoteRepository;


    private Request cacheRe;

    private ChatRepositoryManager(ChatDataSource chatLocalRepository, ChatDataSource chatRemoteRepository) {
        this.chatLocalRepository = chatLocalRepository;
        this.chatRemoteRepository = chatRemoteRepository;
    }

    public static ChatDataSource getInstance(ChatDataSource local, ChatDataSource remote) {
        if (instance == null) {
            instance = new ChatRepositoryManager(local, remote);
            return instance;
        }

        return instance;
    }


    @Override
    public void getMessages(String key, Event1<List<com.example.dmitro.chatapp.data.model.wifiDirect.Message>> messages, Event1<String> failure, Event0 complate) {
        chatRemoteRepository.getMessages(key, messages, failure, complate);

    }

    @Override
    public void sendMessage(String key, Message message, Event1<List<com.example.dmitro.chatapp.data.model.wifiDirect.Message>> s, Event1<String> f, Event0 c) {
        chatRemoteRepository.sendMessage(key, message, s, f, c);

    }



    @Override
    public void getLastMessage(Event1<Request> s, Event1<String> f, Event0 c) {

    }

    @Override
    public void addMessageInDatabase(com.example.dmitro.chatapp.data.model.wifiDirect.Message message, Event1<com.example.dmitro.chatapp.data.model.wifiDirect.Message> s, Event1<String> f, Event0 c) {

    }


}
