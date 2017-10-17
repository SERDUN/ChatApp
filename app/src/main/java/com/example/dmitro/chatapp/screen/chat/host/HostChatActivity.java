package com.example.dmitro.chatapp.screen.chat.host;

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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.dmitro.chatapp.DetectActivity;
import com.example.dmitro.chatapp.R;
import com.example.dmitro.chatapp.connection.sockets.ServerService;
import com.example.dmitro.chatapp.data.model.wifiDirect.Message;
import com.example.dmitro.chatapp.data.model.wifiDirect.request.Request;
import com.example.dmitro.chatapp.data.provider.ContractClass;
import com.example.dmitro.chatapp.data.repository.Injection;
import com.example.dmitro.chatapp.data.repository.managers.WifiDirectChatRepositoryManager;
import com.example.dmitro.chatapp.screen.chat.MessagesRecyclerAdapter;
import com.example.dmitro.chatapp.screen.chat.TCPChatContract;
import com.example.dmitro.chatapp.screen.chat.TCPChatWifiDirectPresenter;
import com.example.dmitro.chatapp.utils.MyUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.dmitro.chatapp.ChatApp.EXTRAS_CONNECT;
import static com.example.dmitro.chatapp.ChatApp.EXTRAS_DISCONNECT;
import static com.example.dmitro.chatapp.ChatApp.EXTRAS_FILE;
import static com.example.dmitro.chatapp.ChatApp.EXTRAS_MESSAGE;

public class HostChatActivity extends DetectActivity implements TCPChatContract.View {
    private static final int SELECT_PHOTO = 913;

    private TCPChatContract.Presenter presenter;

    public static final int BIND_SERVICE_FLAG = 0;
    @BindView(R.id.msgRecyclerView)
    RecyclerView messagesRecyclerView;

    @BindView(R.id.sendMessageButton)
    public ImageButton sendButton;

    @BindView(R.id.attachFileButton)
    public ImageButton attachFileButton;

    @BindView(R.id.messageEditText)
    public EditText messageEditText;

//
//
//    @BindView(R.id.disconnectBT)
//    Button disconnectBT;

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

        attachFileButton.setOnClickListener(v -> {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, SELECT_PHOTO);
        });
        messageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().isEmpty()) {
                    attachFileButton.setVisibility(View.VISIBLE);
                } else {
                    attachFileButton.setVisibility(View.GONE);

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        messagesRecyclerAdapter = new MessagesRecyclerAdapter(MyUtils.Converter.createMessageFromCursor(getContentResolver().query(
                ContractClass.Messages.CONTENT_URI,
                ContractClass.Messages.DEFAULT_PROJECTION,
                null, null,
                null)), message -> {
        });

        messagesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        messagesRecyclerView.setAdapter(messagesRecyclerAdapter);
        messagesRecyclerView.getLayoutManager().scrollToPosition(messagesRecyclerAdapter.getItemCount() - 1);

        sendButton.setOnClickListener(view -> presenter.sendMessage(messageEditText.getText().toString()));

//        disconnectBT.setOnClickListener(view -> {
//            presenter.disconnect();
//            setResult(RESULT_OK, new Intent());
//            finish();
        //  });
        ContentObserver ob = new ContentObserver(new Handler(Looper.getMainLooper())) {
            @Override
            public void onChange(boolean selfChangem, Uri uri) {
                Cursor record = getContentResolver().query(uri,
                        ContractClass.Messages.DEFAULT_PROJECTION,
                        null, null,
                        null);
                messagesRecyclerAdapter.addMessage(MyUtils.Converter.createContentValues(record));
                messagesRecyclerView.getLayoutManager().scrollToPosition(messagesRecyclerAdapter.getItemCount() - 1);

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
    public void sendMessage(Message message) {
        intentService.removeExtra(EXTRAS_FILE);
        intentService.putExtra(EXTRAS_CONNECT, true);
        intentService.putExtra(EXTRAS_MESSAGE, message);
        startService(intentService);
        messageEditText.setText("");
    }

    @Override
    public void disconnect(Request request) {

    }


    @Override
    public void stopService() {
        intentService.putExtra(EXTRAS_DISCONNECT, true);
        startService(intentService);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch (requestCode) {
            case SELECT_PHOTO:
                if (resultCode == RESULT_OK) {
                        Uri selectedImage = imageReturnedIntent.getData();
                        intentService.putExtra(EXTRAS_CONNECT, true);
                        intentService.putExtra(EXTRAS_FILE, selectedImage.toString());
                        startService(intentService);

                }
                break;
        }
    }
}
