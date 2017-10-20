package com.example.dmitro.chatapp.data.model.wifiDirect.socket.data_object;

import java.io.Serializable;

/**
 * Created by dmitro on 18.10.17.
 */

public class Body implements Serializable {
    private String login;
    private long time;
    private byte[] body;
    private Type type;

    public Body(String login, long time, byte[] body, Type type) {
        this.login = login;
        this.time = time;
        this.body = body;
        this.type = type;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
