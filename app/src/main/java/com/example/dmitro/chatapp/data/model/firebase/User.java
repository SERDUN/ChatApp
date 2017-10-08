package com.example.dmitro.chatapp.data.model.firebase;

/**
 * Created by dmitro on 08.10.17.
 */

public class User {
    private String token;
    private String login;
    private String sex;

    public User() {
    }

    public User(String login, String sex) {
        this.login = login;
        this.sex = sex;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}
