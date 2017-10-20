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
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.dmitro.chatapp.DetectActivity;
import com.example.dmitro.chatapp.R;
import com.example.dmitro.chatapp.connection.sockets.ClientService;
import com.example.dmitro.chatapp.connection.sockets.ServerService;
import com.example.dmitro.chatapp.data.model.wifiDirect.socket.data_object.Body;
import com.example.dmitro.chatapp.data.model.wifiDirect.socket.data_object.Type;
import com.example.dmitro.chatapp.data.model.wifiDirect.socket.transport_object.Action;
import com.example.dmitro.chatapp.data.model.wifiDirect.socket.transport_object.Request;
import com.example.dmitro.chatapp.data.provider.ContractClass;

import com.example.dmitro.chatapp.screen.chat.MessagesRecyclerAdapter;
import com.example.dmitro.chatapp.screen.chat.TCPChatContract;
import com.example.dmitro.chatapp.screen.chat.TCPChatWifiDirectPresenter;
import com.example.dmitro.chatapp.screen.chat.client.ChoseTypeFileDialog;
import com.example.dmitro.chatapp.screen.chat.setting_connection.SettingCurrentConnectionActivity;
import com.example.dmitro.chatapp.utils.MyUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.dmitro.chatapp.ChatApp.EXTRAS_CONNECT;
import static com.example.dmitro.chatapp.ChatApp.EXTRAS_DISCONNECT;
import static com.example.dmitro.chatapp.ChatApp.EXTRAS_FILE;
import static com.example.dmitro.chatapp.ChatApp.EXTRAS_MESSAGE;

public class HostChatActivity extends DetectActivity implements TCPChatContract.View {


    private static final int SELECT_PHOTO = 913;
    private static final int SELECT_AUDIO = 914;

    private static final int SETTING_CLIENT_CONNECTION = 800;


    public static final int BIND_SERVICE_FLAG = 0;

    private static final String TYPE_FILE_DIALOG = "type_file_dialog";

    private TCPChatContract.Presenter presenter;


    @BindView(R.id.msgRecyclerView)
    RecyclerView messagesRecyclerView;

    @BindView(R.id.sendMessageButton)
    public ImageButton sendButton;

    @BindView(R.id.messageEditText)
    public EditText messageEditText;


    @BindView(R.id.attachFileButton)
    public ImageButton attachFileButton;


//
//    @BindView(R.id.disconnectBT)
//    Button disconnectBT;

    private MessagesRecyclerAdapter messagesRecyclerAdapter;

    private ServiceConnection serviceConnection;

    private Intent intentService;

    private DialogFragment typeFileDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_chat);
        ButterKnife.bind(this);
        new TCPChatWifiDirectPresenter(this);
        initView();
        catchService();
    }

    private void initView() {
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        typeFileDialog = new ChoseTypeFileDialog();
        ((ChoseTypeFileDialog) typeFileDialog).setClickableListener(id -> {
            switch (id) {
                case R.id.audioBT:
                    openAudioDialog();
                    break;
                case R.id.photoBT:
                    openPhotoDialog();

                    break;
            }
        });
        attachFileButton.setOnClickListener(v -> {
            typeFileDialog.show(getSupportFragmentManager(), TYPE_FILE_DIALOG);
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
        messagesRecyclerAdapter = new MessagesRecyclerAdapter(new ArrayList<>(), message -> {
        });

        messagesRecyclerAdapter = new MessagesRecyclerAdapter(new ArrayList<>(), message -> {
        });

        messagesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        messagesRecyclerView.setAdapter(messagesRecyclerAdapter);

        sendButton.setOnClickListener(view ->
                presenter.sendMessage(messageEditText.getText().toString().getBytes(), Type.TEXT));

//        disconnectBT.setOnClickListener(view -> {
//            presenter.disconnect();
//            setResult(RESULT_OK, new Intent());
//            finish();
//        });
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

    private void openAudioDialog() {
        Intent intent_upload = new Intent();
        intent_upload.setType("audio/*");
        intent_upload.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent_upload, SELECT_AUDIO);
    }

    private void openPhotoDialog() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, SELECT_PHOTO);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.client_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.setup_current_connection: {
                Intent intent = new Intent(this, SettingCurrentConnectionActivity.class);
                startActivityForResult(intent, SETTING_CLIENT_CONNECTION);
                break;
            }
        }
        return false;
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
    public void showMessages(List<Body> FMessages) {

    }

    @Override
    public void sendMessage(Body message) {
        intentService.removeExtra(EXTRAS_FILE);
        intentService.putExtra(EXTRAS_CONNECT, true);
        intentService.putExtra(EXTRAS_MESSAGE, new Request(Action.MESSAGE, message));
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
                    presenter.sendMessage(imageReturnedIntent.getData().toString().getBytes(), Type.URI_PHOTO);

                }
                break;
            case SELECT_AUDIO:
                if (resultCode == RESULT_OK) {
                    presenter.sendMessage(imageReturnedIntent.getData().toString().getBytes(), Type.URI_AUDIO);

                }
                break;

            case SETTING_CLIENT_CONNECTION:
                Toast.makeText(this, "continue work: " + MyUtils.SettingClientConnection.isContinue(), Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
