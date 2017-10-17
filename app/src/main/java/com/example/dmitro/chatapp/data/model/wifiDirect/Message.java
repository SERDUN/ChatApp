package com.example.dmitro.chatapp.data.model.wifiDirect;

import java.io.Serializable;

import io.realm.annotations.PrimaryKey;

/**
 * Created by dmitro on 11.10.17.
 */

public class Message implements Serializable {
    private String author;
    private String message;
    private byte[] file;
    private String uri;



    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    @PrimaryKey
    private long time;

    public Message() {
    }

    public Message(String author, String message, long time) {
        this.author = author;
        this.message = message;
        this.time = time;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
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
