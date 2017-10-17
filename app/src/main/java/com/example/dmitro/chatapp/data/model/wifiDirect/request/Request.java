package com.example.dmitro.chatapp.data.model.wifiDirect.request;

import com.example.dmitro.chatapp.data.model.wifiDirect.Action;
import com.example.dmitro.chatapp.data.model.wifiDirect.Message;
import com.example.dmitro.chatapp.data.model.wifiDirect.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dmitro on 09.10.17.
 */

public class Request implements Serializable {
    private Action action;
    private Message message;
    private ArrayList<Message> messages;

    public Request(Action action) {

        this.action = action;
    }

    public Action getAction() {
        return action;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }

    public void setAction(Action action) {
        this.action = action;
    }


}
