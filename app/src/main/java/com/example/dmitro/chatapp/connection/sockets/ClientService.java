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

import com.example.dmitro.chatapp.ChatApp;
import com.example.dmitro.chatapp.R;
import com.example.dmitro.chatapp.data.model.wifiDirect.socket.data_object.Body;
import com.example.dmitro.chatapp.data.model.wifiDirect.socket.data_object.Type;
import com.example.dmitro.chatapp.data.model.wifiDirect.socket.transport_object.Action;
import com.example.dmitro.chatapp.data.model.wifiDirect.socket.transport_object.Request;
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
import static com.example.dmitro.chatapp.ChatApp.EXTRAS_GROUP_OWNER_ADDRESS;
import static com.example.dmitro.chatapp.ChatApp.EXTRAS_GROUP_OWNER_PORT;
import static com.example.dmitro.chatapp.ChatApp.EXTRAS_MESSAGE;
import static com.example.dmitro.chatapp.screen.ChatConst.ACTION_SERVICE_MANIPULATE_KEY;
import static com.example.dmitro.chatapp.screen.ChatConst.SEND_DATA;
import static com.example.dmitro.chatapp.screen.ChatConst.SOCKET_CONNECTION;
import static com.example.dmitro.chatapp.screen.ChatConst.SOCKET_DISCONNECT;

public class ClientService extends Service {

    String TAG = "ClientServiceLOG";

    private Socket socket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        switch (intent.getStringExtra(ACTION_SERVICE_MANIPULATE_KEY)) {

            case SOCKET_CONNECTION:
                createConnection(intent);
                break;
            case SOCKET_DISCONNECT:
                disconnect();
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
        sendMessage(request);

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

    private void disconnect() {
//        try {
//            sendMessage(new Request(Action.DISCONNECT));
//            objectInputStream.close();
//            objectOutputStream.flush();
//            objectOutputStream.close();
//            socket.close();
//            stopSelf();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
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
                Request request = new Request(Action.ALL_MESSAGE);
                request.setBody(new Body(MyUtils.WIFIDirect.getCurrentUser().getLogin(), System.currentTimeMillis(), "".getBytes(), Type.TEXT));
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
                    switch (request.getAction()) {
                        case ALL_MESSAGE:
                            saveAllMessages(request);
                            break;
                        case MESSAGE:
                            saveMessage(request.getBody());
                            break;

                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }).start();

    }

    private void saveAllMessages(Request request) {
        for (Body message : request.getMessages()) {
            saveMessage(message);
        }


    }

    private void saveMessage(Body message) {
        switch (message.getType()) {
            case PHOTO:
                message.setType(Type.URI_PHOTO);
                message.setBody(StorageUtils.saveToInternalStorage(BitmapFactory.decodeByteArray(message.getBody(), 0, message.getBody().length)).getBytes());
                break;
            case AUDIO:
                message.setType(Type.URI_AUDIO);
                message.setBody(StorageUtils.saveToInternalStorage(message.getBody()).getBytes());
                break;
        }
        getContentResolver().insert(ContractClass.Messages.CONTENT_URI,
                MyUtils.Converter.createContentValues(message));
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new Binder();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }
}
