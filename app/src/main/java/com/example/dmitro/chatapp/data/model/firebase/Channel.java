package com.example.dmitro.chatapp.data.model.firebase;

import java.util.HashMap;
import java.util.List;

/**
 * Created by dmitro on 08.10.17.
 */

public class Channel {
    public String chanelName;
    public List<User> users;
    public HashMap<String,Message> messages;


    public Channel() {
    }

//    public Channel(String chanelName, List<User> users, List<Message> messages) {
//        this.chanelName = chanelName;
//        this.users = users;
//        this.messages = messages;
//    }
//
//    public void addMessage(Message message){
//        messages.add(message);
//    }
}
