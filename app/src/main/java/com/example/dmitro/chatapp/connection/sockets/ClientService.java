// Copyright 2011 Google Inc. All Rights Reserved.

package com.example.dmitro.chatapp.connection.sockets;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.dmitro.chatapp.R;
import com.example.dmitro.chatapp.data.model.wifiDirect.Action;
import com.example.dmitro.chatapp.data.model.wifiDirect.Message;
import com.example.dmitro.chatapp.data.model.wifiDirect.request.Request;
import com.example.dmitro.chatapp.data.provider.ContractClass;
import com.example.dmitro.chatapp.utils.MyUtils;
import com.example.dmitro.chatapp.utils.StorageUtils;

import org.apache.commons.io.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import static com.example.dmitro.chatapp.ChatApp.EXTRAS_CONNECT;
import static com.example.dmitro.chatapp.ChatApp.EXTRAS_DISCONNECT;
import static com.example.dmitro.chatapp.ChatApp.EXTRAS_FILE;
import static com.example.dmitro.chatapp.ChatApp.EXTRAS_GROUP_OWNER_ADDRESS;
import static com.example.dmitro.chatapp.ChatApp.EXTRAS_GROUP_OWNER_PORT;
import static com.example.dmitro.chatapp.ChatApp.EXTRAS_MESSAGE;

public class ClientService extends Service {

    String TAG = "ClientServiceLOG";

    private Socket socket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getBooleanExtra(EXTRAS_DISCONNECT, false)) {
            disconnect();
        } else if (intent.getBooleanExtra(EXTRAS_CONNECT, false)) {
            generateRequest(intent);
        } else {
            createConnection(intent);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void generateRequest(Intent intent) {
        Request request = new Request(null);
        request.setMessage((Message) intent.getSerializableExtra(EXTRAS_MESSAGE));
        if (intent.getStringExtra(EXTRAS_FILE) != null) {
            InputStream imageStream = null;
            try {
                imageStream = getContentResolver().openInputStream(Uri.parse(intent.getStringExtra(EXTRAS_FILE)));
                Bitmap img = BitmapFactory.decodeStream(imageStream);
                Message message = new Message(MyUtils.WIFIDirect.getCurrentUser().getLogin(), "", System.currentTimeMillis());

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                img.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                message.setFile(byteArray);
                request.setMessage(message);


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        sendMessage(request);

    }

    private void disconnect() {
        try {
            sendMessage(new Request(Action.DISCONNECT));
            objectInputStream.close();
            objectOutputStream.flush();
            objectOutputStream.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void sendMessage(Request request) {
        try {
            objectOutputStream.flush();
            objectOutputStream.writeObject(request);
            objectOutputStream.flush();
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
                socket.connect((new InetSocketAddress(host, port)), Integer.parseInt(getString(R.string.socket_timeout)));
                objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                objectInputStream = new ObjectInputStream(socket.getInputStream());
                listenerServer(objectInputStream);
                Request request = new Request(Action.GET_ALL_MESSAGE);
                request.setMessage(new Message(MyUtils.WIFIDirect.getCurrentUser().getLogin(), "", System.currentTimeMillis()));
                objectOutputStream.writeObject(request);
                objectOutputStream.flush();


            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            } finally {

            }
        }).start();
    }


    private void listenerServer(ObjectInputStream o) {
        new Thread(() -> {
            try {
                while (true) {
                    Request request = (Request) o.readObject();
                    if (request.getAction() == Action.GET_ALL_MESSAGE) {
                        ///// TODO: 16.10.17 replace at transaction
                        for (Message message : request.getMessages()) {
                            if(message.getFile()!=null){
                                String uri = StorageUtils.saveToInternalStorage(BitmapFactory.decodeByteArray(message.getFile(), 0, message.getFile().length));
                                message.setUri(uri);
                            }

                            getContentResolver().insert(ContractClass.Messages.CONTENT_URI,
                                    MyUtils.Converter.createContentValues(message));
                        }
                        Log.d(TAG, "listenerServer: ");
                    } else {
                        if(request.getMessage().getFile()!=null){
                            String uri = StorageUtils.saveToInternalStorage(BitmapFactory.decodeByteArray(request.getMessage().getFile(), 0, request.getMessage().getFile().length));
                            request.getMessage().setUri(uri);
                        }
                        getContentResolver().insert(ContractClass.Messages.CONTENT_URI,
                                MyUtils.Converter.createContentValues(request.getMessage()));
                    }
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
