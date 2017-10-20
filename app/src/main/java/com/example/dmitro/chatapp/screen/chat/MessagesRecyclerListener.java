package com.example.dmitro.chatapp.screen.chat;

import com.example.dmitro.chatapp.data.model.wifiDirect.socket.data_object.Body;

/**
 * Created by dmitro on 15.10.17.
 */

public interface MessagesRecyclerListener {
    public void call(Body channelKey);

}
