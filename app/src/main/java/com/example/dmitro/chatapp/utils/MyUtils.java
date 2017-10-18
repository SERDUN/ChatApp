package com.example.dmitro.chatapp.utils;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;

import com.example.dmitro.chatapp.ChatApp;
import com.example.dmitro.chatapp.R;
import com.example.dmitro.chatapp.connection.TypeConnection;
import com.example.dmitro.chatapp.data.model.wifiDirect.User;
import com.example.dmitro.chatapp.data.model.wifiDirect.socket.data_object.Body;
import com.example.dmitro.chatapp.data.model.wifiDirect.socket.data_object.Type;
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
        public static ContentValues createContentValues(Body body) {
            ContentValues value = new ContentValues();
            value.put(ContractClass.Messages.COLUMN_NAME_LOGIN, body.getLogin());
            value.put(ContractClass.Messages.COLUMN_NAME_TIME, body.getTime());
            value.put(ContractClass.Messages.COLUMN_NAME_TYPE, body.getType().toString());
            value.put(ContractClass.Messages.COLUMN_NAME_BODY, body.getBody());
            return value;
        }


//
//        String s = ;
//        byte[] arr = StorageUtils.loadByteArrayFromStorage(s);


        //         if (type == Type.URI_PHOTO) {
////// TODO: 18.10.17 replace
//            ByteArrayOutputStream stream = new ByteArrayOutputStream();
//            StorageUtils.loadImageFromStorage(new String(body)).compress(Bitmap.CompressFormat.PNG, 100, stream);
//            byte[] byteArray = stream.toByteArray();
//
//            messageObj.setType(Type.PHOTO);
//            messageObj.setBody(byteArray);
//        }
        public static Body createContentValues(Cursor record) {
            Body messageObj = null;
            if (record.getCount() != 0) {
                if (record.moveToFirst()) {
                    do {
                        String r = record.getString(record.getColumnIndex(ContractClass.Messages.COLUMN_NAME_TYPE));
                        Type type = Type.fromString(r);
                        String login = record.getString(record.getColumnIndex(ContractClass.Messages.COLUMN_NAME_LOGIN));
                        byte[] body = record.getBlob(record.getColumnIndex(ContractClass.Messages.COLUMN_NAME_BODY));
                        long time = record.getLong(record.getColumnIndex(ContractClass.Messages.COLUMN_NAME_TIME));
                        messageObj = new Body(login, time, body, type);

                        switch (type) {
                            case URI_PHOTO:
                                replacePhotoIriOnData(body, messageObj);
                                break;
                            case URI_AUDIO:
//                                replaceAudioIriOnData(body, messageObj);
                                break;
                        }

                    } while (record.moveToNext());
                }
            }
            record.close();

            return messageObj;
        }

        private static void replacePhotoIriOnData(byte[] uri, Body messageObj) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            StorageUtils.loadImageFromStorage(new String(uri)).compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();

            messageObj.setType(Type.PHOTO);
            messageObj.setBody(byteArray);
        }


        private static void replaceAudioIriOnData(byte[] uri, Body messageObj) {
            byte[] byteArray = StorageUtils.loadByteArrayFromStorage(new String(uri));
            messageObj.setType(Type.AUDIO);
            messageObj.setBody(byteArray);
        }


        public static ArrayList<Body> createMessageFromCursor(Cursor record) {
            ArrayList<Body> messages = new ArrayList<>();
            if (record.getCount() != 0) {
                if (record.moveToFirst()) {
                    do {
                        Type type = Type.fromString(record.getString(record.getColumnIndex(ContractClass.Messages.COLUMN_NAME_TYPE)));
                        String login = record.getString(record.getColumnIndex(ContractClass.Messages.COLUMN_NAME_LOGIN));
                        byte[] body = record.getBlob(record.getColumnIndex(ContractClass.Messages.COLUMN_NAME_BODY));
                        long time = record.getLong(record.getColumnIndex(ContractClass.Messages.COLUMN_NAME_TIME));

                        Body message = new Body(login, time, body, type);
                        switch (type) {
                            case URI_PHOTO:
                                replacePhotoIriOnData(body, message);
                                break;
                            case URI_AUDIO:
                                replaceAudioIriOnData(body, message);
                                break;
                        }
                        messages.add(message);
                    } while (record.moveToNext());
                }
            }
            record.close();

            return messages;
        }


    }


}
