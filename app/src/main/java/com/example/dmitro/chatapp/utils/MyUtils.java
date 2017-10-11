package com.example.dmitro.chatapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.dmitro.chatapp.ChatApp;
import com.example.dmitro.chatapp.R;
import com.example.dmitro.chatapp.connection.TypeConnection;
import com.example.dmitro.chatapp.data.model.wifiDirect.User;

/**
 * Created by dmitro on 08.10.17.
 */

public class MyUtils {

    public static TypeConnection getCurrentTypeCoonection() {
        SharedPreferences sharedPref = ChatApp.getInstance().getSharedPreferences(ChatApp.getInstance().getString(R.string.connection_conf),
                Context.MODE_PRIVATE);
        String typeCoonnection = sharedPref.getString(ChatApp.getInstance().getString(R.string.type_connection), "");

        switch (typeCoonnection) {
            case "Firebase connection":
                return TypeConnection.CONNECT_TO_SERVER_FIREBASE;
            case "Tomcat connection":
                return TypeConnection.CONNECT_TO_SERVER_TOMCAT;
            case "WifiDirect connection":
                return TypeConnection.CONNECT_TO_WIFI_DIRECT;
            case "TCP_IP connection":
                return TypeConnection.CONNECT_TO_TCP_IP;
            default:
                return TypeConnection.CONNECT_TO_SERVER_FIREBASE;
        }

    }


    public static class WIFIDirect {
        public static boolean isServer() {
            SharedPreferences sharedPref = ChatApp.getInstance().getSharedPreferences(ChatApp.getInstance().getString(R.string.connection_conf),
                    Context.MODE_PRIVATE);
            boolean isServer = sharedPref.getBoolean(ChatApp.getInstance().getString(R.string.isServer), false);
            return isServer;
        }

        public static void setServerType() {
            SharedPreferences sharedPref = ChatApp.getInstance().getSharedPreferences(ChatApp.getInstance().getString(R.string.connection_conf),
                    Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean(ChatApp.getInstance().getString(R.string.isServer), true);
            editor.apply();

        }

        public static User getCurrentUser() {
            SharedPreferences sharedPref = ChatApp.getInstance().getSharedPreferences(ChatApp.getInstance().getString(R.string.connection_conf),
                    Context.MODE_PRIVATE);
            String name = sharedPref.getString(ChatApp.getInstance().getString(R.string.user_name), "");
            return new User(name);

        }

    }


}
