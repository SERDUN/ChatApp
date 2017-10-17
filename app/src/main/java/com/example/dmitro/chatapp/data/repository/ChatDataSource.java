package com.example.dmitro.chatapp.data.repository;

import com.example.dmitro.chatapp.data.model.wifiDirect.Message;
import com.example.dmitro.chatapp.data.model.wifiDirect.request.Request;
import com.example.dmitro.chatapp.utils.event.Event0;
import com.example.dmitro.chatapp.utils.event.Event1;

import java.util.List;

/**
 * Created by dmitro on 08.10.17.
 */

public interface ChatDataSource {

    public void getMessages(String key, Event1<List<Message>> messages, Event1<String> failure, Event0 complate);

    public void sendMessage(String key, Message message, Event1<List<Message>> s, Event1<String> f, Event0 c);

    public void getLastMessage(Event1<Request> s, Event1<String> f, Event0 c);

    void addMessageInDatabase(com.example.dmitro.chatapp.data.model.wifiDirect.Message message, Event1<com.example.dmitro.chatapp.data.model.wifiDirect.Message> s, Event1<String> f, Event0 c);


}
