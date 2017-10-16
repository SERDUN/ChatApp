package com.example.dmitro.chatapp.screen.chat.tcp_ip;
import com.example.dmitro.chatapp.data.model.wifiDirect.Message;

/**
 * Created by dmitro on 15.10.17.
 */

public interface MessagesRecyclerListener {
    public void call(Message channelKey);

}
