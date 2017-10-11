package com.example.dmitro.chatapp.data.model.wifiDirect;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by dmitro on 11.10.17.
 */

public class Message extends RealmObject {
    private String author;
    private String message;

    @PrimaryKey
    private long time;

    public Message() {
    }

    public Message(String author, String message, long time) {
        this.author = author;
        this.message = message;
        this.time = time;
    }

    public static Message getInstanceFromRequest(Request request) {
        return new Message(request.getAuthor().toString(), request.getMessage(), request.getTime());
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
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
        return "Message{" +
                "author=" + author +
                ", message='" + message + '\'' +
                ", time=" + time +
                '}';
    }
}
