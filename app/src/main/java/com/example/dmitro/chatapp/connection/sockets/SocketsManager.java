package com.example.dmitro.chatapp.connection.sockets;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.BitmapFactory;

import com.example.dmitro.chatapp.data.model.wifiDirect.User;
import com.example.dmitro.chatapp.data.model.wifiDirect.socket.data_object.Body;
import com.example.dmitro.chatapp.data.model.wifiDirect.socket.data_object.Type;
import com.example.dmitro.chatapp.data.model.wifiDirect.socket.transport_object.Action;
import com.example.dmitro.chatapp.data.model.wifiDirect.socket.transport_object.Request;
import com.example.dmitro.chatapp.data.provider.ContractClass;
import com.example.dmitro.chatapp.utils.MyUtils;
import com.example.dmitro.chatapp.utils.Observable;
import com.example.dmitro.chatapp.utils.Observer;
import com.example.dmitro.chatapp.utils.StorageUtils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by dmitro on 09.10.17.
 */

public class SocketsManager implements Observable {

    private static SocketsManager INSTANCE;

    private LinkedList<ObjectInputStream> inputStreams;
    private LinkedList<ObjectOutputStream> outputStreams;
    private LinkedList<Socket> sockets;
    private ContentResolver contentResolver;
    private LinkedList<Observer> observables;

    private ArrayList<User> users;

    private SocketsManager(ContentResolver contentResolver) {
        this.inputStreams = new LinkedList<>();
        this.outputStreams = new LinkedList<>();
        this.sockets = new LinkedList<>();
        this.contentResolver = contentResolver;
        this.observables = new LinkedList<>();
        this.users = new ArrayList<>();
    }


    public static SocketsManager getInstance(ContentResolver contentResolver) {
        if (INSTANCE == null)
            INSTANCE = new SocketsManager(contentResolver);
        return INSTANCE;
    }


    public void addUser(Socket socket) {
        try {
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            inputStreams.add(in);
            outputStreams.add(out);
            sockets.add(socket);

            new Thread(new ListenUser(in, out, socket)).start();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    class ListenUser implements Runnable {
        private Socket socket;
        private User user;

        ObjectOutputStream outputStream;
        ObjectInputStream inputStream;

        public ListenUser(ObjectInputStream in, ObjectOutputStream out, Socket socket) {
            this.inputStream = in;
            this.outputStream = out;
            this.socket = socket;
        }

        @Override
        public void run() {
            Request object = null;

            while (true) {

                try {
                    object = (Request) inputStream.readObject();


                    switch (object.getAction()) {
                        case ALL_MESSAGE:
                            sendAllMessage(outputStream);
                            break;
                        case MESSAGE:
                            notifyAllAboutMessage(socket, object.getBody());

                            break;

                        case DISCONNECT:
                            inputStream.close();
                            outputStream.close();
                            socket.close();
                            inputStreams.remove(inputStream);
                            outputStreams.remove(outputStream);
                            sockets.remove(socket);
                            users.remove(user);
                            break;
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

        private void sendAllMessage(ObjectOutputStream outputStream) {
            Cursor c = contentResolver.query(
                    ContractClass.Messages.CONTENT_URI,
                    ContractClass.Messages.DEFAULT_PROJECTION,
                    null, null,
                    null);
            try {
                Request request = new Request(Action.ALL_MESSAGE);
                ArrayList<Body> arrayList = MyUtils.Converter.createMessageFromCursor(c);
                request.setBody(arrayList);
                outputStream.writeObject(request);
                outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    public synchronized void notifyAllAboutMessage(Socket sender, Body message) {
        Type typeTmp = message.getType();
        byte[] bodyTmp = message.getBody();


        switch (message.getType()) {
            case PHOTO:
                message.setType(Type.URI_PHOTO);
                message.setBody(StorageUtils.saveToInternalStorage(BitmapFactory.decodeByteArray(message.getBody(), 0, message.getBody().length)).getBytes());
                ContentValues contentValues = MyUtils.Converter.createContentValues(message);
                contentResolver.insert(ContractClass.Messages.CONTENT_URI, contentValues);
                break;
            case AUDIO:
                break;
            case TEXT:
                break;
        }

        message.setBody(bodyTmp);
        message.setType(typeTmp);

        new Thread(() -> {
            try {
                for (ObjectOutputStream out : outputStreams) {
                    Request request = new Request(Action.MESSAGE);
                    request.setBody(message);
                    out.writeObject(request);

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }


    public synchronized void notifyAllAboutMessage(Body message) {
        Type typeTmp = message.getType();
        byte[] bodyTmp = message.getBody();


        switch (message.getType()) {
            case PHOTO:
                message.setType(Type.URI_PHOTO);
                message.setBody(StorageUtils.saveToInternalStorage(BitmapFactory.decodeByteArray(message.getBody(), 0, message.getBody().length)).getBytes());
                ContentValues contentValues = MyUtils.Converter.createContentValues(message);
                contentResolver.insert(ContractClass.Messages.CONTENT_URI, contentValues);
                break;
            case AUDIO:
                break;
            case TEXT:
                break;
        }

        message.setBody(bodyTmp);
        message.setType(typeTmp);

        new Thread(() -> {
            try {
                for (ObjectOutputStream out : outputStreams) {
                    Request request = new Request(Action.MESSAGE);
                    request.setBody(message);
                    out.writeObject(request);

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

    }


    public LinkedList<Socket> getSockets() {
        return sockets;
    }

    @Override
    public void registerObserver(Observer o) {
        observables.add(o);
    }

    @Override
    public void removeObserver(Observer o) {
        observables.remove(o);
    }

    @Override
    public <ArrayList> void notifyObserver(ArrayList t) {
        for (Observer observer : observables) {
            observer.update(users);
        }
    }


}
