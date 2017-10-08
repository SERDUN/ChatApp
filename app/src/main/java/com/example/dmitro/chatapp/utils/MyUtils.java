package com.example.dmitro.chatapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.dmitro.chatapp.ChatApp;
import com.example.dmitro.chatapp.R;
import com.example.dmitro.chatapp.connection.TypeConnection;

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
            default:
                return TypeConnection.CONNECT_TO_SERVER_FIREBASE;
        }

    }
}
