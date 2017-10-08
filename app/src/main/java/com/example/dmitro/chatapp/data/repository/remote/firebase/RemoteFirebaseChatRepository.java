package com.example.dmitro.chatapp.data.repository.remote.firebase;

import com.example.dmitro.chatapp.data.model.firebase.Channel;
import com.example.dmitro.chatapp.data.model.firebase.ChannelKey;
import com.example.dmitro.chatapp.data.model.firebase.Message;
import com.example.dmitro.chatapp.data.repository.ChatDataSource;
import com.example.dmitro.chatapp.utils.event.Event0;
import com.example.dmitro.chatapp.utils.event.Event1;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dmitro on 08.10.17.
 */

public class RemoteFirebaseChatRepository implements ChatDataSource {
    private final String TAG = "ChatRepository_log";
    private static RemoteFirebaseChatRepository instance;
    private FirebaseDatabase firebaseDatabase;

    private RemoteFirebaseChatRepository() {
        firebaseDatabase = FirebaseDatabase.getInstance();
    }

    public static RemoteFirebaseChatRepository getInstance() {
        if (instance == null) instance = new RemoteFirebaseChatRepository();
        return instance;
    }

    @Override
    public void getListChannel(Event1<List<ChannelKey>> channels, Event1<String> failure, Event0 complate) {
        DatabaseReference listRef = firebaseDatabase.getInstance().getReference("Channels");
        listRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<ChannelKey> channelKeys = new ArrayList<ChannelKey>();
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {

                    Channel channel = userSnapshot.getValue(Channel.class);
                    String key = userSnapshot.getKey();
                    channelKeys.add(new ChannelKey(channel.chanelName, key));
                }
                channels.call(channelKeys);
                complate.call();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                failure.call(databaseError.getMessage());
                complate.call();
            }
        });
    }

    @Override
    public void getMessages(String key, Event1<List<Message>> channels, Event1<String> failure, Event0 complate) {
        DatabaseReference listRef = firebaseDatabase.getInstance().getReference("Channels").child(key).child("messages");
        listRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    List<Message> messages = new ArrayList<Message>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    messages.add(postSnapshot.getValue(Message.class));
                }
                channels.call(messages);
                complate.call();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                failure.call(databaseError.getMessage());
                complate.call();

            }
        });

    }

    @Override
    public void sendMessage(String key, Message message, Event1<List<Message>> s, Event1<String> f, Event0 c) {
        DatabaseReference listRef = firebaseDatabase.getInstance().getReference("Channels").child(key).child("messages");
        String id = listRef.push().getKey();
        listRef.child(id).setValue(message);
        listRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Message> messages = new ArrayList<Message>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    messages.add(postSnapshot.getValue(Message.class));
                    s.call(messages);
                    c.call();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                f.call(databaseError.getMessage());
                c.call();
            }
        });

    }
}
