package com.example.dmitro.chatapp.utils;

import com.example.dmitro.chatapp.data.model.wifiDirect.Message;
import com.example.dmitro.chatapp.data.model.wifiDirect.Request;

import java.util.List;

/**
 * Created by dmitro on 11.10.17.
 */

public interface Observer {
    <T> void update(T t);

}
