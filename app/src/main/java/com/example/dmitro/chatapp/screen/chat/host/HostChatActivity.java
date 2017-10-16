package com.example.dmitro.chatapp.screen.chat.host;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.EditText;

import com.example.dmitro.chatapp.R;
import com.example.dmitro.chatapp.connection.sockets.ServerService;
import com.example.dmitro.chatapp.data.model.wifiDirect.Message;
import com.example.dmitro.chatapp.data.model.wifiDirect.request.Request;
import com.example.dmitro.chatapp.data.provider.ContractClass;
import com.example.dmitro.chatapp.data.repository.Injection;
import com.example.dmitro.chatapp.data.repository.managers.WifiDirectChatRepositoryManager;
import com.example.dmitro.chatapp.screen.chat.tcp_ip.MessagesRecyclerAdapter;
import com.example.dmitro.chatapp.screen.chat.tcp_ip.TCPChatContract;
import com.example.dmitro.chatapp.screen.chat.tcp_ip.TCPChatWifiDirectPresenter;
import com.example.dmitro.chatapp.utils.MyUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.dmitro.chatapp.ChatApp.EXTRAS_CONNECT;
import static com.example.dmitro.chatapp.ChatApp.EXTRAS_DISCONNECT;
import static com.example.dmitro.chatapp.ChatApp.EXTRAS_MESSAGE;

public class HostChatActivity extends AppCompatActivity  implements TCPChatContract.View  {

    private TCPChatContract.Presenter presenter;

    public static final int BIND_SERVICE_FLAG = 0;
    @BindView(R.id.tcpMessagesTV)
    RecyclerView messagesRecyclerView;

    @BindView(R.id.stcpSendButton)
    public Button sendButton;

    @BindView(R.id.tcpTextMessageEdit)
    public EditText messageEditText;


    @BindView(R.id.disconnectBT)
    Button disconnectBT;

    private MessagesRecyclerAdapter messagesRecyclerAdapter;

    private ServiceConnection serviceConnection;

    private Intent intentService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_chat);
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

        disconnectBT.setOnClickListener(view -> {
            presenter.disconnect();
            setResult(RESULT_OK, new Intent());
            finish();
        });
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


            intentService = new Intent(this, ServerService.class);


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

    @Override
    public void stopService() {
        intentService.putExtra(EXTRAS_DISCONNECT, true);
        startService(intentService);
    }
}
