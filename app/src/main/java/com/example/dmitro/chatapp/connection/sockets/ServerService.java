package com.example.dmitro.chatapp.connection.sockets;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.dmitro.chatapp.R;
import com.example.dmitro.chatapp.data.model.wifiDirect.request.Request;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import static com.example.dmitro.chatapp.ChatApp.BROADCAST_CONNECT_ACTION;
import static com.example.dmitro.chatapp.ChatApp.EXTRAS_CONNECT;
import static com.example.dmitro.chatapp.ChatApp.EXTRAS_GROUP_OWNER_PORT;
import static com.example.dmitro.chatapp.ChatApp.EXTRAS_MESSAGE;
import static com.example.dmitro.chatapp.ChatApp.STATUS_SERVER_STARTED_SUCCESS;

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

        new Thread(() -> {
            try {
                socketsManager = SocketsManager.getInstance(getContentResolver());
                ServerSocket serverSocket = new ServerSocket(intent.getIntExtra(EXTRAS_GROUP_OWNER_PORT, Integer.parseInt(getString(R.string.default_port))));
                //сповіщаю setting activity про успішне створення хоста
                Intent in = new Intent(BROADCAST_CONNECT_ACTION);
                in.putExtra(EXTRAS_CONNECT, STATUS_SERVER_STARTED_SUCCESS);
                sendBroadcast(in);

                while (true) {
                    Socket client = serverSocket.accept();
                    socketsManager.addUser(client);

                }
            } catch (IOException e) {
                Log.d("hhh", "createServer: ");
//                try {
//                   // pendingIntent.send(this, STATUS_SERVER_STARTED_FAILURE, null);
//                } catch (PendingIntent.CanceledException e1) {
//                    e1.printStackTrace();
//                }
//            } catch (PendingIntent.CanceledException e) {
//                e.printStackTrace();
            }
        }).start();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
