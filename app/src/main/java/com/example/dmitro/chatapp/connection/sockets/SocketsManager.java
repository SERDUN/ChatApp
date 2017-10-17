package com.example.dmitro.chatapp.connection.sockets;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.BitmapFactory;

import com.example.dmitro.chatapp.data.model.wifiDirect.Action;
import com.example.dmitro.chatapp.data.model.wifiDirect.Message;
import com.example.dmitro.chatapp.data.model.wifiDirect.User;
import com.example.dmitro.chatapp.data.model.wifiDirect.request.Request;
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
            try {
                Request object = (Request) inputStream.readObject();

                if (object.getAction() == Action.GET_ALL_MESSAGE) {
                    sendAllMessage(outputStream);
                }
//                object.setMessage("Увійшо в чат");
//                user = new User(object.getAuthor().getLogin());
//                users.add(user);
//                notifyObserver(null);
//                notifyAllAboutMessage(socket, object);

                while (true) {
                    object = (Request) inputStream.readObject();

                    if (object.getAction() == Action.DISCONNECT) {
                        inputStream.close();
                        outputStream.close();
                        socket.close();
                        inputStreams.remove(inputStream);
                        outputStreams.remove(outputStream);
                        sockets.remove(socket);
                        users.remove(user);
                    } else {
                        notifyAllAboutMessage(socket, object.getMessage());

                    }


                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        private void sendAllMessage(ObjectOutputStream outputStream) {
            Cursor c = contentResolver.query(
                    ContractClass.Messages.CONTENT_URI,
                    ContractClass.Messages.DEFAULT_PROJECTION,
                    null, null,
                    null);
            try {
                Request request = new Request(Action.GET_ALL_MESSAGE);
                ArrayList<Message> arrayList = MyUtils.Converter.createMessageFromCursor(c);
                request.setMessages(arrayList);
                outputStream.writeObject(request);
                outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    public synchronized void notifyAllAboutMessage(Socket sender, Message message) {
        if (message.getFile() != null) {
            String uri = StorageUtils.saveToInternalStorage(BitmapFactory.decodeByteArray(message.getFile(), 0, message.getFile().length));
            message.setUri(uri);
        }

        ContentValues contentValues = MyUtils.Converter.createContentValues(message);
        contentResolver.insert(ContractClass.Messages.CONTENT_URI, contentValues);
        try {
            for (ObjectOutputStream out : outputStreams) {
                Request request = new Request(null);
                request.setMessage(message);
                out.writeObject(request);
                out.flush();

            }

            // }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public synchronized void notifyAllAboutMessage(Message message) {
        if (message.getFile() != null) {
            String uri = StorageUtils.saveToInternalStorage(BitmapFactory.decodeByteArray(message.getFile(), 0, message.getFile().length));
            message.setUri(uri);
        }

        ContentValues contentValues = MyUtils.Converter.createContentValues(message);
        contentResolver.insert(ContractClass.Messages.CONTENT_URI, contentValues);

        new Thread(() -> {
            try {
                for (ObjectOutputStream out : outputStreams) {
                    Request request = new Request(null);
                    request.setMessage(message);
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
