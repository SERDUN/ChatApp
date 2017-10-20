package com.example.dmitro.chatapp.data.model.wifiDirect.socket.data_object;

/**
 * Created by dmitro on 18.10.17.
 */

public enum Type {
    TEXT("TEXT"),
    TEXT_LIST("TEXT_LIST"),
    PHOTO(""),
    PHOTO_LIST("PHOTO"),
    AUDIO("AUDIO"),
    AUDIO_LIST("AUDIO_LIST"),
    URI_PHOTO("URI_PHOTO"),
    URI_AUDIO("URI_AUDIO");

    private String type;

    Type(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

    public static Type fromString(String text) {
        for (Type b : Type.values()) {
            if (b.type.equalsIgnoreCase(text)) {
                return b;
            }
        }
        return null;
    }


}
