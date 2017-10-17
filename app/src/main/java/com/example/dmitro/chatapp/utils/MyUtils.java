package com.example.dmitro.chatapp.utils;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;

import com.example.dmitro.chatapp.ChatApp;
import com.example.dmitro.chatapp.R;
import com.example.dmitro.chatapp.connection.TypeConnection;
import com.example.dmitro.chatapp.data.model.wifiDirect.Message;
import com.example.dmitro.chatapp.data.model.wifiDirect.User;
import com.example.dmitro.chatapp.data.provider.ContractClass;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * Created by dmitro on 08.10.17.
 */

public class MyUtils {


    public static class SettingClientConnection {
        public static boolean isContinue() {
            SharedPreferences sharedPref = ChatApp.getInstance().getSharedPreferences(ChatApp.getInstance().getString(R.string.setting_client),
                    Context.MODE_PRIVATE);
            boolean continueWork = sharedPref.getBoolean(ChatApp.getInstance().getString(R.string.continue_work_client_connection), false);
            return continueWork;
        }

    }

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


    public static class Converter {
        public static ContentValues createContentValues(Message message) {
            ContentValues value = new ContentValues();
            value.put(ContractClass.Messages.COLUMN_NAME_IS_NEW_USER, "old");
            value.put(ContractClass.Messages.COLUMN_NAME_LOGIN, message.getAuthor());
            value.put(ContractClass.Messages.COLUMN_NAME_TIME, message.getTime());
            value.put(ContractClass.Messages.COLUMN_NAME_MESSAGE, message.getMessage());
            value.put(ContractClass.Messages.COLUMN_NAME_FILE_URI, message.getUri());
            return value;
        }


        public static Message createContentValues(Cursor record) {
            Message messageObj = null;
            if (record.getCount() != 0) {
                if (record.moveToFirst()) {
                    do {

                        String file = record.getString(record.getColumnIndex(ContractClass.Messages.COLUMN_NAME_FILE_URI));
                        String login = record.getString(record.getColumnIndex(ContractClass.Messages.COLUMN_NAME_LOGIN));
                        String message = record.getString(record.getColumnIndex(ContractClass.Messages.COLUMN_NAME_MESSAGE));
                        long time = record.getLong(record.getColumnIndex(ContractClass.Messages.COLUMN_NAME_TIME));
                        messageObj = new Message(login, message, time);
                        if (file != null) {
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            StorageUtils.loadImageFromStorage(file).compress(Bitmap.CompressFormat.PNG, 100, stream);
                            byte[] byteArray = stream.toByteArray();
                            messageObj.setFile(byteArray);
                        }
                    } while (record.moveToNext());
                }
            }
            record.close();

            return messageObj;
        }


        public static ArrayList<Message> createMessageFromCursor(Cursor record) {
            ArrayList<Message> messages = new ArrayList<>();
            if (record.getCount() != 0) {
                if (record.moveToFirst()) {
                    do {
                        String file = record.getString(record.getColumnIndex(ContractClass.Messages.COLUMN_NAME_FILE_URI));
                        String login = record.getString(record.getColumnIndex(ContractClass.Messages.COLUMN_NAME_LOGIN));
                        String message = record.getString(record.getColumnIndex(ContractClass.Messages.COLUMN_NAME_MESSAGE));
                        long time = record.getLong(record.getColumnIndex(ContractClass.Messages.COLUMN_NAME_TIME));
                        Message message1 = new Message(login, message, time);
                        if (file != null) {
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            StorageUtils.loadImageFromStorage(file).compress(Bitmap.CompressFormat.PNG, 100, stream);
                            byte[] byteArray = stream.toByteArray();
                            message1.setFile(byteArray);
                        }
                        messages.add(message1);
                    } while (record.moveToNext());
                }
            }
            record.close();

            return messages;
        }


    }


}
