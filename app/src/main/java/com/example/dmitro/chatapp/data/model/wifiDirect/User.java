package com.example.dmitro.chatapp.data.model.wifiDirect;

import java.io.Serializable;

/**
 * Created by dmitro on 09.10.17.
 */

public class User implements Serializable {
    public User(String login) {
        this.login = login;
    }

    private String login;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Override
    public String toString() {
        return "User{" +
                "login='" + login + '\'' +
                '}';
    }
}
