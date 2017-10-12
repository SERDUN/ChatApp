package com.example.dmitro.chatapp.connection.sockets;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.example.dmitro.chatapp.R;
import com.example.dmitro.chatapp.data.model.wifiDirect.Request;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import static com.example.dmitro.chatapp.ChatApp.EXTRAS_CONNECT;
import static com.example.dmitro.chatapp.ChatApp.EXTRAS_GROUP_OWNER_PORT;
import static com.example.dmitro.chatapp.ChatApp.PARAM_PINTENT;
import static com.example.dmitro.chatapp.ChatApp.STATUS_SERVER_STARTED_FEILURE;
import static com.example.dmitro.chatapp.ChatApp.STATUS_SERVER_STARTED_SUCCESS;
import static com.example.dmitro.chatapp.screen.chat.wifi_direct.ChatActivity.EXTRAS_MESSAGE;

/**
 * Created by dmitro on 09.10.17.
 */

public class ServerService extends Service {
    private SocketsManager socketsManager;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent.getBooleanExtra(EXTRAS_CONNECT, false)) {
            sendMessage(intent);
        } else {
            createServer(intent);
        }

        return super.onStartCommand(intent, flags, startId);
    }

    private void sendMessage(Intent intent) {
        socketsManager.notifyAllAboutMessage((Request) intent.getSerializableExtra(EXTRAS_MESSAGE));
    }

    private void createServer(Intent intent) {
        PendingIntent pendingIntent = intent.getParcelableExtra(PARAM_PINTENT);

        new Thread(() -> {
            try {
                socketsManager = SocketsManager.getInstance(getContentResolver());
                ServerSocket serverSocket = new ServerSocket(intent.getIntExtra(EXTRAS_GROUP_OWNER_PORT, Integer.parseInt(getString(R.string.default_port))));
                //сповіщаю setting activity про успішне створення хоста
                pendingIntent.send(STATUS_SERVER_STARTED_SUCCESS);
                while (true) {
                    Socket client = serverSocket.accept();
                    socketsManager.addUser(client);

                }
            } catch (IOException e) {
                try {
                    pendingIntent.send(this, STATUS_SERVER_STARTED_FEILURE, null);
                } catch (PendingIntent.CanceledException e1) {
                    e1.printStackTrace();
                }
            } catch (PendingIntent.CanceledException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
