package com.example.dmitro.chatapp.connection.wifiDirectionHost;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.dmitro.chatapp.data.model.wifiDirect.Request;
import com.example.dmitro.chatapp.data.model.wifiDirect.User;
import com.example.dmitro.chatapp.utils.MyUtils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import static com.example.dmitro.chatapp.connection.wifiDirectionHost.ClientService.EXTRAS_CONNECT;
import static com.example.dmitro.chatapp.screen.chat.ChatActivity.EXTRAS_MESSAGE;

/**
 * Created by dmitro on 09.10.17.
 */

public class ServerService extends Service {
    public static String LOG = "server_log";
    public UserManager userManager;

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
        userManager.resenderMessage(new Request(MyUtils.WIFIDirect.getCurrentUser(),intent.getStringExtra(EXTRAS_MESSAGE),System.currentTimeMillis()));
    }

    private void createServer(Intent intent) {
        new Thread(() -> {
            try {
                userManager = new UserManager();
                ServerSocket serverSocket = new ServerSocket(8988);

                while (true) {
                    Socket client = serverSocket.accept();
                    userManager.addUser(client);

                }

            } catch (IOException e) {
//                Log.e(WiFiDirectActivity.TAG, e.getMessage());
            }
        }).start();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private static String getStringFromInputStream(InputStream is) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();

    }

    public static boolean copyFile(InputStream inputStream, OutputStream out) {
        byte buf[] = new byte[1024];
        int len;
        try {
            while ((len = inputStream.read(buf)) != -1) {
                out.write(buf, 0, len);

            }
            out.close();
            inputStream.close();
        } catch (IOException e) {
//            Log.d(WiFiDirectActivity.TAG, e.toString());
            return false;
        }
        return true;
    }
}
