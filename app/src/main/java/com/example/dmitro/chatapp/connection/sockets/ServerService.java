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
import com.example.dmitro.chatapp.data.model.wifiDirect.socket.data_object.Body;
import com.example.dmitro.chatapp.data.model.wifiDirect.socket.data_object.Type;
import com.example.dmitro.chatapp.data.model.wifiDirect.socket.transport_object.Request;

import org.apache.commons.io.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

import static com.example.dmitro.chatapp.ChatApp.BROADCAST_CONNECT_ACTION;
import static com.example.dmitro.chatapp.ChatApp.EXTRAS_CONNECT;
import static com.example.dmitro.chatapp.ChatApp.EXTRAS_GROUP_OWNER_PORT;
import static com.example.dmitro.chatapp.ChatApp.EXTRAS_MESSAGE;
import static com.example.dmitro.chatapp.ChatApp.STATUS_SERVER_STARTED_SUCCESS;
import static com.example.dmitro.chatapp.screen.ChatConst.ACTION_SERVICE_MANIPULATE_KEY;
import static com.example.dmitro.chatapp.screen.ChatConst.SEND_DATA;
import static com.example.dmitro.chatapp.screen.ChatConst.SOCKET_CONNECTION;
import static com.example.dmitro.chatapp.screen.ChatConst.SOCKET_DISCONNECT;

/**
 * Created by dmitro on 09.10.17.
 */

public class ServerService extends Service {
    private SocketsManager socketsManager;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        switch (intent.getStringExtra(ACTION_SERVICE_MANIPULATE_KEY)) {
            case SOCKET_CONNECTION:
                createServer(intent);
                break;
            case SOCKET_DISCONNECT:
                break;
            case SEND_DATA:
                generateRequest(intent);
                break;
        }

        return super.onStartCommand(intent, flags, startId);
    }
        private void generateRequest(Intent intent) {
            Request request = (Request) intent.getSerializableExtra(EXTRAS_MESSAGE);
            switch (request.getBody().getType()) {
                case URI_PHOTO:
                    replacePhotoUriOnData(getStreamByUri(Uri.parse(new String(request.getBody().getBody()))), request);
                    break;
                case URI_AUDIO:
                    replaceAudioUriOnData(getStreamByUri(Uri.parse(new String(request.getBody().getBody()))), request);
                    break;
            }
            sendMessage(request.getBody());


    }


    private void replacePhotoUriOnData(InputStream imgStream, Request request) {
        Bitmap img = BitmapFactory.decodeStream(imgStream);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        img.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        request.getBody().setType(Type.PHOTO);
        request.getBody().setBody(byteArray);
    }

    private void replaceAudioUriOnData(InputStream audioStream, Request request) {
        try {
            byte[] byteArray = IOUtils.toByteArray(audioStream);
            request.getBody().setType(Type.AUDIO);
            request.getBody().setBody(byteArray);
        } catch (IOException e) {


        }

    }

    private InputStream getStreamByUri(Uri uri) {
        InputStream data = null;
        try {
            data = getContentResolver().openInputStream(uri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return data;
    }
    private void sendMessage(Body message) {
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

            }
        }).start();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
