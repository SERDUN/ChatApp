package com.example.dmitro.chatapp.screen.chat;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.EditText;

import com.example.dmitro.chatapp.R;
import com.example.dmitro.chatapp.data.model.firebase.Message;
import com.example.dmitro.chatapp.data.repository.Injection;
import com.example.dmitro.chatapp.screen.setting.firebase.ChannelFirebaseActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatActivity extends AppCompatActivity implements ChatContract.View {
    private ChatContract.Presenter presenter;

    @BindView(R.id.messagesTV)
    RecyclerView messagesRecyclerView;

    @BindView(R.id.sendButton)
    Button sendButton;

    @BindView(R.id.textMessageEdit)
    EditText messageEditText;



    MessagesRecyclerAdapter messagesRecyclerAdapter;

    String key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        Intent intent = getIntent();
         key = intent.getStringExtra(ChannelFirebaseActivity.KEY_CHANNEL);
        new ChatPresenter(this, Injection.provideManager(), getSharedPreferences(getString(R.string.connection_conf), Context.MODE_PRIVATE), key);
        initView();
        presenter.getMessages();
    }

    private void initView() {
        messagesRecyclerAdapter = new MessagesRecyclerAdapter(new ArrayList<>(), message -> {
        });
        messagesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        messagesRecyclerView.setAdapter(messagesRecyclerAdapter);
        sendButton.setOnClickListener(v->{
            presenter.sendMessage(messageEditText.getText().toString());
        });

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference listRef = firebaseDatabase.getInstance().getReference("Channels").child(key).child("messages");
        
        listRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Message> messages = new ArrayList<Message>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    messages.add(postSnapshot.getValue(Message.class));
                }
                showMessages(messages);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
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
    public void showMessages(List<Message> messages) {
        messagesRecyclerAdapter.updateData((ArrayList<Message>) messages);
    }
}
