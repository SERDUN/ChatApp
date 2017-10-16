package com.example.dmitro.chatapp.data.model.wifiDirect.request;

import com.example.dmitro.chatapp.data.model.wifiDirect.Action;
import com.example.dmitro.chatapp.data.model.wifiDirect.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dmitro on 09.10.17.
 */

public class Request implements Serializable {
    protected User author;
    protected long time;
    protected Action action;

    public Request(User author, long time, Action action) {
        this.author = author;
        this.time = time;
        this.action = action;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }


    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Request{" +
                "author=" + author +
                ", time=" + time +
                '}';
    }
}
