package com.example.dmitro.chatapp.data.repository;

import com.example.dmitro.chatapp.data.model.firebase.ChannelKey;
import com.example.dmitro.chatapp.data.model.firebase.Message;
import com.example.dmitro.chatapp.utils.event.Event0;
import com.example.dmitro.chatapp.utils.event.Event1;

import java.util.List;

/**
 * Created by dmitro on 08.10.17.
 */

public class ChatRepositoryManager implements ChatDataSource {
    private static ChatRepositoryManager instance = null;
    private final ChatDataSource chatLocalRepository;
    private final ChatDataSource chatRemoteRepository;

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
    public void getListChannel(Event1<List<ChannelKey>> channels, Event1<String> failure, Event0 complate) {
        chatRemoteRepository.getListChannel(channels, failure, complate);
    }

    @Override
    public void getMessages(String key, Event1<List<Message>> channels, Event1<String> failure, Event0 complate) {
        chatRemoteRepository.getMessages(key, channels, failure, complate);

    }

    @Override
    public void sendMessage(String key, Message message, Event1<List<Message>> s, Event1<String> f, Event0 c) {
        chatRemoteRepository.sendMessage(key, message, s, f, c);
    }
}
