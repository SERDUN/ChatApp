// Copyright 2011 Google Inc. All Rights Reserved.

package com.example.dmitro.chatapp.connection.wifiDirectionHost;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.dmitro.chatapp.data.model.wifiDirect.Message;
import com.example.dmitro.chatapp.data.model.wifiDirect.Request;
import com.example.dmitro.chatapp.data.repository.Injection;
import com.example.dmitro.chatapp.data.repository.managers.WifiDirectChatRepositoryManager;
import com.example.dmitro.chatapp.utils.MyUtils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import static com.example.dmitro.chatapp.screen.chat.ChatActivity.EXTRAS_DISCONNECT;
import static com.example.dmitro.chatapp.screen.chat.ChatActivity.EXTRAS_MESSAGE;

public class ClientService extends Service {

    String TAG = "ClientServiceLOG";

    private static final int SOCKET_TIMEOUT = 5000;
    public static final String EXTRAS_GROUP_OWNER_ADDRESS = "go_host";
    public static final String EXTRAS_GROUP_OWNER_PORT = "go_port";
    public static final String EXTRAS_CONNECT = "go_connect";


    private Socket socket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getBooleanExtra(EXTRAS_DISCONNECT, false)) {
            try {
                objectInputStream.close();
                objectOutputStream.flush();
                objectOutputStream.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        if (intent.getBooleanExtra(EXTRAS_CONNECT, false)) {
            sendMessage(intent);
        } else {
            createConnection(intent);
        }
        return super.onStartCommand(intent, flags, startId);
//// TODO: 10.10.17 close socket
    }


    private void sendMessage(Intent intent) {
        try {
            objectOutputStream.flush();
            Request request = new Request(MyUtils.WIFIDirect.getCurrentUser(), intent.getStringExtra(EXTRAS_MESSAGE), System.currentTimeMillis());
            objectOutputStream.writeObject(request);
            objectOutputStream.flush();
//            ServerService.copyFile(in, objectOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void createConnection(Intent intent) {
        new Thread(() -> {
            socket = new Socket();
            int port = intent.getExtras().getInt(EXTRAS_GROUP_OWNER_PORT);
            String host = intent.getExtras().getString(EXTRAS_GROUP_OWNER_ADDRESS);


            try {
                Log.d(TAG, "Opening client socket - ");
                socket.bind(null);
                socket.connect((new InetSocketAddress(host, port)), SOCKET_TIMEOUT);
                objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                objectInputStream = new ObjectInputStream(socket.getInputStream());
                listenerServer(objectInputStream);
                Request request = new Request(MyUtils.WIFIDirect.getCurrentUser(), "", System.currentTimeMillis());
                objectOutputStream.writeObject(request);
                objectOutputStream.flush();


            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            } finally {

            }
        }).start();
    }


    private void listenerServer(ObjectInputStream o) {

        WifiDirectChatRepositoryManager manager = (WifiDirectChatRepositoryManager) Injection.provideManager();
        new Thread(() -> {
            try {
                while (true) {
                    Request request = (Request) o.readObject();
                    Log.d(TAG, "listenerServer: " + request.toString());
                    manager.addMessageInDatabase(Message.getInstanceFromRequest(request), null, null, null);

                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }).start();

    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new Binder();
    }
}
