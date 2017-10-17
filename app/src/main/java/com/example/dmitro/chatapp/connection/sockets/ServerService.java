package com.example.dmitro.chatapp.connection.sockets;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.dmitro.chatapp.R;
import com.example.dmitro.chatapp.data.model.wifiDirect.Message;
import com.example.dmitro.chatapp.data.model.wifiDirect.request.Request;
import com.example.dmitro.chatapp.utils.MyUtils;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

import static com.example.dmitro.chatapp.ChatApp.BROADCAST_CONNECT_ACTION;
import static com.example.dmitro.chatapp.ChatApp.EXTRAS_CONNECT;
import static com.example.dmitro.chatapp.ChatApp.EXTRAS_FILE;
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

            generateRequest(intent);
        } else {
            createServer(intent);
        }

        return super.onStartCommand(intent, flags, startId);
    }
    private void generateRequest(Intent intent) {
        Message message=(Message) intent.getSerializableExtra(EXTRAS_MESSAGE);

        if (intent.getStringExtra(EXTRAS_FILE) != null) {
            InputStream imageStream = null;
            try {
                imageStream = getContentResolver().openInputStream(Uri.parse(intent.getStringExtra(EXTRAS_FILE)));
                Bitmap img = BitmapFactory.decodeStream(imageStream);
                 message = new Message(MyUtils.WIFIDirect.getCurrentUser().getLogin(), "", System.currentTimeMillis());

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                img.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                message.setFile(byteArray);


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        sendMessage(message);

    }
    private void sendMessage(Message message) {
        socketsManager.notifyAllAboutMessage(message);
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
