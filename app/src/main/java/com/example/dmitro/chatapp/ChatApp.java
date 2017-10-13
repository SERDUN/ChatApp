package com.example.dmitro.chatapp;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.firebase.database.FirebaseDatabase;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by dmitro on 08.10.17.
 */

public class ChatApp extends Application {
    public static final String EXTRAS_GROUP_OWNER_PORT = "go_port";
    public static final String EXTRAS_GROUP_OWNER_ADDRESS = "go_host";
    public static final String EXTRAS_CONNECT = "go_connect";
    public static final String EXTRAS_DISCONNECT = "go_disconnect";



    private static ChatApp instance;


    public final static int STATUS_SERVER_STARTED_SUCCESS = 101;
    public final static int STATUS_SERVER_STARTED_FAILURE = 102;

    public static final int REQUEST_CODE_FOR_CHAT = 1;



    public final static String PARAM_PINTENT = "pendingIntent";



    public final static String LOG_COUNT_CONNECTED="log_count_connected";

    public static ChatApp getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .name(Realm.DEFAULT_REALM_NAME)
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);

    }
}
