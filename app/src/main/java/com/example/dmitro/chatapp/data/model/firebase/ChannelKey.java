package com.example.dmitro.chatapp.data.model.firebase;

import java.util.List;

/**
 * Created by dmitro on 08.10.17.
 */

public class ChannelKey {
    private String nameChannel;
    private String key;

    public ChannelKey() {
    }

    public ChannelKey(String nameChannel, String key) {
        this.nameChannel = nameChannel;
        this.key = key;
    }

    public String getNameChannel() {
        return nameChannel;
    }

    public void setNameChannel(String nameChannel) {
        this.nameChannel = nameChannel;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
