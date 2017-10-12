package com.example.dmitro.chatapp.connection.sockets;

import android.content.ContentResolver;

import com.example.dmitro.chatapp.data.model.wifiDirect.Message;
import com.example.dmitro.chatapp.data.model.wifiDirect.Request;
import com.example.dmitro.chatapp.data.model.wifiDirect.User;
import com.example.dmitro.chatapp.data.provider.ContractClass;
import com.example.dmitro.chatapp.utils.MyUtils;
import com.example.dmitro.chatapp.utils.Observable;
import com.example.dmitro.chatapp.utils.Observer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

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

    private SocketsManager(ContentResolver contentResolver) {
        this.inputStreams = new LinkedList<>();
        this.outputStreams = new LinkedList<>();
        this.sockets = new LinkedList<>();
        this.contentResolver = contentResolver;
        this.observables = new LinkedList<>();
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
                object.setMessage("Увійшо в чат");
                notifyAllAboutMessage(socket, object);

                while (true) {
                    object = (Request) inputStream.readObject();
                    notifyAllAboutMessage(socket, object);

                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }


    }

    public synchronized void notifyAllAboutMessage(Socket sender, Request request) {
        contentResolver.insert(ContractClass.Messages.CONTENT_URI,
                MyUtils.Converter.createContentValues(Message.getInstanceFromRequest(request)));
        try {
            for (ObjectOutputStream out : outputStreams) {
                out.writeObject(request);
                out.flush();

            }

            // }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public synchronized void notifyAllAboutMessage(Request request) {
        contentResolver.insert(ContractClass.Messages.CONTENT_URI,
                MyUtils.Converter.createContentValues(Message.getInstanceFromRequest(request)));


        new Thread(() -> {
            try {
                for (ObjectOutputStream out : outputStreams) {
                    out.writeObject(request);
                    out.flush();

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
    public <T> void notifyObserver(T t) {
        for (Observer observer : observables) {
            observer.update(null);
        }
    }


}
