package com.example.dmitro.chatapp;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by dmitro on 08.10.17.
 */

public class ChatApp extends Application {
    private static ChatApp instance;

    public static ChatApp getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

    }
}
