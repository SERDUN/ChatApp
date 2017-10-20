package com.example.dmitro.chatapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.dmitro.chatapp.ChatApp;
import com.example.dmitro.chatapp.R;

import static com.example.dmitro.chatapp.screen.ChatConst.IS_SERVICE_CLIENT_RUNNING;
import static com.example.dmitro.chatapp.screen.ChatConst.IS_SERVICE_SERVER_RUNNING;

/**
 * Created by dmitro on 19.10.17.
 */

public abstract class ServiceUtil {
    public static class Server {
        public static boolean isServiceServerIsRunning() {
            SharedPreferences sharedPref = ChatApp.getInstance().getSharedPreferences(ChatApp.getInstance().getString(R.string.connection_conf),
                    Context.MODE_PRIVATE);
            boolean isServer = sharedPref.getBoolean(IS_SERVICE_SERVER_RUNNING, false);
            return isServer;
        }

        public static void serviceServerIsRunning() {
            SharedPreferences sharedPref = ChatApp.getInstance().getSharedPreferences(ChatApp.getInstance().getString(R.string.connection_conf),
                    Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean(IS_SERVICE_SERVER_RUNNING, true);
            editor.apply();

        }
    }

    public static class Client {
        public static boolean isServiceClientIsRunning() {
            SharedPreferences sharedPref = ChatApp.getInstance().getSharedPreferences(ChatApp.getInstance().getString(R.string.connection_conf),
                    Context.MODE_PRIVATE);
            boolean isServer = sharedPref.getBoolean(IS_SERVICE_CLIENT_RUNNING, false);
            return isServer;
        }

        public static void serviceClientIsRunning() {
            SharedPreferences sharedPref = ChatApp.getInstance().getSharedPreferences(ChatApp.getInstance().getString(R.string.connection_conf),
                    Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean(IS_SERVICE_CLIENT_RUNNING, true);
            editor.apply();

        }
    }
}
