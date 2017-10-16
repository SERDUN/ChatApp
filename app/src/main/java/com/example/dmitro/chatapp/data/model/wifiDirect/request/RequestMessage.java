package com.example.dmitro.chatapp.data.model.wifiDirect.request;

import com.example.dmitro.chatapp.data.model.wifiDirect.Action;
import com.example.dmitro.chatapp.data.model.wifiDirect.Message;
import com.example.dmitro.chatapp.data.model.wifiDirect.User;

import java.io.Serializable;

/**
 * Created by dmitro on 16.10.17.
 */

public class RequestMessage extends Request implements Serializable {
    private Message message;

    public RequestMessage(Message message, User author, long time, Action action) {
        super(author, time, action);
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}