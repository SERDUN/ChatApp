package com.example.dmitro.chatapp.data.model.firebase;

/**
 * Created by dmitro on 08.10.17.
 */

public class Message {
    private long time;
    private User author;
    private String text;

    public Message() {
    }

    public Message(long time, User author, String text) {
        this.time = time;
        this.author = author;
        this.text = text;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
