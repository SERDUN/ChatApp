package com.example.dmitro.chatapp.screen.chat.wifi_direct;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dmitro.chatapp.R;
import com.example.dmitro.chatapp.connection.sockets.ClientService;
import com.example.dmitro.chatapp.connection.sockets.ServerService;
import com.example.dmitro.chatapp.data.model.wifiDirect.Message;
import com.example.dmitro.chatapp.data.model.wifiDirect.Request;
import com.example.dmitro.chatapp.data.repository.Injection;
import com.example.dmitro.chatapp.data.repository.managers.WifiDirectChatRepositoryManager;
import com.example.dmitro.chatapp.screen.chat.wifi_direct.presenters.ChatWifiDirectPresenter;
import com.example.dmitro.chatapp.utils.MyUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.dmitro.chatapp.ChatApp.EXTRAS_CONNECT;

public class ChatActivity extends AppCompatActivity implements ChatContract.View {
    private ChatContract.Presenter presenter;

    public static final String EXTRAS_DISCONNECT = "go_disconnect";


    @BindView(R.id.messagesTV)
    RecyclerView messagesRecyclerView;

    @BindView(R.id.sendButton)
    Button sendButton;

    @BindView(R.id.textMessageEdit)
    EditText messageEditText;

    @BindView(R.id.isServerTV)
    TextView isServerTextView;


    public static String EXTRAS_MESSAGE = "message";

    MessagesRecyclerAdapter messagesRecyclerAdapter;

    public static String TAG = "ChatActivityLog";


    String key;

    Intent intent;

    ServiceConnection serviceConnectionClient;
    ServiceConnection serviceConnectionServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        ///////////
        if (MyUtils.WIFIDirect.isServer()) {
            isServerTextView.setText("server");
            intent = new Intent(this, ServerService.class);
            serviceConnectionServer = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName componentName, IBinder iBinder) {


                }

                @Override
                public void onServiceDisconnected(ComponentName componentName) {

                }

            };
            bindService(intent, serviceConnectionServer, 0);


        } else {
            isServerTextView.setText("client");
            intent = new Intent(this, ClientService.class);
            serviceConnectionClient = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                    Log.d(TAG, "MainActivity onServiceConnected");

                }

                @Override
                public void onServiceDisconnected(ComponentName componentName) {
                    Log.d(TAG, "MainActivity onServiceConnected");

                }

            };

            bindService(intent, serviceConnectionClient, 0);

        }

        new ChatWifiDirectPresenter(this, (WifiDirectChatRepositoryManager) Injection.provideManager());
        initView();
    }

    private void initView() {
        messagesRecyclerAdapter = new MessagesRecyclerAdapter(new ArrayList<>(), message -> {
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);

        messagesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        messagesRecyclerView.setAdapter(messagesRecyclerAdapter);


        sendButton.setOnClickListener(v -> {
            if (MyUtils.WIFIDirect.isServer()) {
                intent.putExtra(EXTRAS_CONNECT, true);
                intent.putExtra(EXTRAS_MESSAGE, new Request(MyUtils.WIFIDirect.getCurrentUser(), messageEditText.getText().toString(), System.currentTimeMillis()));
                startService(intent);
                messageEditText.setText("");
            } else {
                intent.putExtra(EXTRAS_CONNECT, true);
                intent.putExtra(EXTRAS_MESSAGE, new Request(MyUtils.WIFIDirect.getCurrentUser(), messageEditText.getText().toString(), System.currentTimeMillis()));
                startService(intent);
                messageEditText.setText("");
            }

        });

    }

    @Override
    public void initPresenter(ChatContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showMessage(int id) {

    }

    @Override
    public void updateShowMessage(Request request) {
        Toast.makeText(this, "last message" + request.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showMessages(List<Message> messages) {

        runOnUiThread(() -> {
            messagesRecyclerAdapter.updateData((ArrayList<Message>) messages);
            messagesRecyclerView.smoothScrollToPosition(messagesRecyclerAdapter.getItemCount() - 1);
            messagesRecyclerAdapter.notifyDataSetChanged();
        });


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        intent.putExtra(EXTRAS_DISCONNECT,true);
        startService(intent);
    }
}
