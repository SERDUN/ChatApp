package com.example.dmitro.chatapp.data.model.wifiDirect.socket.transport_object;

import com.example.dmitro.chatapp.data.model.wifiDirect.socket.data_object.Body;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dmitro on 09.10.17.
 */

public class Request implements Serializable {
    private Action action;
    private List<Body> body;

    public Request(Action action, Body body) {
        this.action = action;
        this.body = new ArrayList<>();
        this.body.add(body);
    }

    public Request(Action action, List<Body> body) {
        this.action = action;
        this.body = body;
    }

    public Request(Action action) {
        this.action = action;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public Body getBody() {
        return body.get(0);
    }

    public List<Body> getMessages() {
        return body;
    }

    public void setBody(Body body) {
        this.body.add(body);
    }

    public void setBody(List<Body> body) {
        this.body = body;
    }
}
