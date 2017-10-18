package com.example.dmitro.chatapp;

import android.app.Application;

import com.example.dmitro.chatapp.utils.event.Event0;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Timer;
import java.util.TimerTask;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by dmitro on 08.10.17.
 */

public class ChatApp extends Application {


    private Event0 event;

    public Event0 getEvent() {
        return event;
    }

    public void setEvent(Event0 event) {
        this.event = event;
    }

    public static final String EXTRAS_GROUP_OWNER_PORT = "go_port";
    public static final String EXTRAS_GROUP_OWNER_ADDRESS = "go_host";
    public static final String EXTRAS_CONNECT = "go_connect";
    public static final String EXTRAS_DISCONNECT = "go_disconnect";
    public static final String EXTRAS_FILE = "extras_file";
    public static String EXTRAS_MESSAGE = "message";

    public final static String BROADCAST_CONNECT_ACTION = "com.example.dmitro.chatapp.BROADCAST_CONNECT_ACTION";


    private static ChatApp instance;


    public final static int STATUS_SERVER_STARTED_SUCCESS = 101;
    public final static int STATUS_SERVER_STARTED_FAILURE = 102;

    public static final int REQUEST_CODE_FOR_CHAT = 1;

    public final static String PARAM_PINTENT = "pendingIntent";

    public final static String LOG_COUNT_CONNECTED = "log_count_connected";

    public boolean isAppRunning = true;
    public final int timerRate = 500;    // Execute timer task every 500mS

    public void setIsAppRunning(boolean v) {
        isAppRunning = v;
    }

    public boolean isAppRunning() {
        return isAppRunning;
    }

    public static ChatApp getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        Timer mTimer = new Timer();

        mTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (!isAppRunning)
                    if(event!=null)event.call();

                }
        }, 0, timerRate);


    }
}
