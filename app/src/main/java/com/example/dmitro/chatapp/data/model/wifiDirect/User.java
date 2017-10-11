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

    @Override
    public String toString() {
        return "User{" +
                "login='" + login + '\'' +
                '}';
    }
}
