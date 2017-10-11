package com.example.dmitro.chatapp.connection.wifiDirectionHost;

import android.util.Log;

import com.example.dmitro.chatapp.data.model.wifiDirect.Message;
import com.example.dmitro.chatapp.data.model.wifiDirect.Request;
import com.example.dmitro.chatapp.data.model.wifiDirect.User;
import com.example.dmitro.chatapp.data.repository.Injection;
import com.example.dmitro.chatapp.data.repository.managers.WifiDirectChatRepositoryManager;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;

/**
 * Created by dmitro on 09.10.17.
 */

public class UserManager {

    private LinkedList<ObjectInputStream> inputStreams;
    private LinkedList<ObjectOutputStream> outputStreams;
    private LinkedList<Socket> sockets;

    public static String LOG = "server_log";

    public UserManager() {
        inputStreams = new LinkedList<>();
        outputStreams = new LinkedList<>();
        sockets = new LinkedList<>();
    }

    public void addUser(Socket socket) {
        try {
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            inputStreams.add(in);
            outputStreams.add(out);
            sockets.add(socket);

            new Thread(new ListenUser(in, out,socket)).start();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    class ListenUser implements Runnable {
        private Socket socket;
        private User user;

        ObjectOutputStream outputStream;
        ObjectInputStream inputStream;

        public ListenUser(ObjectInputStream in, ObjectOutputStream out,Socket socket) {
            this.inputStream = in;
            this.outputStream = out;
            this.socket=socket;
        }

        @Override
        public void run() {
            try {


                Request object = (Request) inputStream.readObject();
                object.setMessage("Увійшо в чат");
                resenderMessage(socket, object);

                while (true) {
                    object = (Request) inputStream.readObject();
                    resenderMessage(socket, object);

                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }


    }

    public synchronized void resenderMessage(Socket sender, Request request) {
        WifiDirectChatRepositoryManager manager = (WifiDirectChatRepositoryManager) Injection.provideManager();
        manager.addMessageInDatabase(Message.getInstanceFromRequest(request), null, null, null);
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


    public synchronized void resenderMessage(Request request) {
          WifiDirectChatRepositoryManager manager = (WifiDirectChatRepositoryManager) Injection.provideManager();
          manager.addMessageInDatabase(Message.getInstanceFromRequest(request), null, null, null);

        new Thread(() -> {
            try {
                for (ObjectOutputStream out : outputStreams) {
                    out.writeObject(request);
                    out.flush();

                }

                // }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

    }

}
