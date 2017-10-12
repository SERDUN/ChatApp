package com.example.dmitro.chatapp.screen.chat.tcp_ip;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.EditText;

import com.example.dmitro.chatapp.R;
import com.example.dmitro.chatapp.connection.sockets.ClientService;
import com.example.dmitro.chatapp.connection.sockets.ServerService;
import com.example.dmitro.chatapp.data.model.wifiDirect.Message;
import com.example.dmitro.chatapp.data.model.wifiDirect.Request;
import com.example.dmitro.chatapp.data.provider.ContractClass;
import com.example.dmitro.chatapp.data.repository.Injection;
import com.example.dmitro.chatapp.data.repository.managers.WifiDirectChatRepositoryManager;
import com.example.dmitro.chatapp.screen.chat.wifi_direct.MessagesRecyclerAdapter;
import com.example.dmitro.chatapp.utils.MyUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.dmitro.chatapp.ChatApp.EXTRAS_CONNECT;
import static com.example.dmitro.chatapp.screen.chat.wifi_direct.ChatActivity.EXTRAS_MESSAGE;

public class TCPChatActivity extends AppCompatActivity implements TCPChatContract.View {
    private TCPChatContract.Presenter presenter;

    public static final int BIND_SERVICE_FLAG = 0;
    @BindView(R.id.tcpMessagesTV)
    RecyclerView messagesRecyclerView;

    @BindView(R.id.stcpSendButton)
    public Button sendButton;

    @BindView(R.id.tcpTextMessageEdit)
    public EditText messageEditText;

    private MessagesRecyclerAdapter messagesRecyclerAdapter;

    private ServiceConnection serviceConnection;

    private Intent intentService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tcpchat);
        ButterKnife.bind(this);
        new TCPChatWifiDirectPresenter(this, (WifiDirectChatRepositoryManager) Injection.provideManager());
        initView();
        catchService();
    }

    private void initView() {
        messagesRecyclerAdapter = new MessagesRecyclerAdapter(new ArrayList<>(), message -> {
        });

        messagesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        messagesRecyclerView.setAdapter(messagesRecyclerAdapter);

        sendButton.setOnClickListener(view -> presenter.sendMessage(messageEditText.getText().toString()));


        ContentObserver ob = new ContentObserver(new Handler(Looper.getMainLooper())) {
            @Override
            public void onChange(boolean selfChangem, Uri uri) {
                Cursor record = getContentResolver().query(uri,
                        ContractClass.Messages.DEFAULT_PROJECTION,
                        null, null,
                        null);
                messagesRecyclerAdapter.addMessage(MyUtils.Converter.createContentValues(record));
            }
        };
        getContentResolver().registerContentObserver(ContractClass.Messages.CONTENT_URI, true, ob);
    }

    private void catchService() {

        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
            }
        };


        if (MyUtils.WIFIDirect.isServer())
            intentService = new Intent(this, ServerService.class);
        else
            intentService = new Intent(this, ClientService.class);


        bindService(intentService, serviceConnection, BIND_SERVICE_FLAG);
    }

    @Override
    public void initPresenter(TCPChatContract.Presenter presenter) {
        this.presenter = presenter;

    }

    @Override
    public void showMessage(int id) {

    }

    @Override
    public void updateShowMessage(Request request) {

    }

    @Override
    public void showMessages(List<Message> FMessages) {

    }

    @Override
    public void sendMessage(Request request) {
        intentService.putExtra(EXTRAS_CONNECT, true);
        intentService.putExtra(EXTRAS_MESSAGE, request);
        startService(intentService);
        messageEditText.setText("");
    }
}
