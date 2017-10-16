package com.example.dmitro.chatapp.data.model.wifiDirect.request;

import com.example.dmitro.chatapp.data.model.wifiDirect.Action;
import com.example.dmitro.chatapp.data.model.wifiDirect.Message;
import com.example.dmitro.chatapp.data.model.wifiDirect.User;

import java.util.ArrayList;

/**
 * Created by dmitro on 16.10.17.
 */

public class RequestListMessage extends Request {

    private ArrayList<Message> messages;

    public RequestListMessage(ArrayList<Message> messages, User author, long time, Action action) {
        super(author, time, action);
        this.messages = messages;
    }


    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }
}
