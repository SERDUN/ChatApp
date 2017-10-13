package com.example.dmitro.chatapp.data.model.wifiDirect;

import java.io.Serializable;

/**
 * Created by dmitro on 09.10.17.
 */

public class Request implements Serializable {

    public Request() {
    }

    public Request(Action action) {
        this.action = action;
    }

    public Request(User author, String message, long time) {
        this.author = author;
        this.message = message;
        this.time = time;
    }

    private User author;
    private String message;
    private long time;
    private Action action;

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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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
                ", message='" + message + '\'' +
                ", time=" + time +
                '}';
    }
}
