package com.example.dmitro.chatapp.test;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.example.dmitro.chatapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
//    List<User> users1 = new LinkedList();
//    List<Message> messages1 = new LinkedList();
//
//
//    List<User> users2 = new LinkedList();
//    List<Message> messages2 = new LinkedList();
//
//
//    FirebaseDatabase database;
//
//
//    User user1 = new User("name 1", "email 1");
//    DatabaseReference myRef;

    IntentFilter intentFilter;

    @BindView(R.id.search_pir)
    Button searchPir;


    WifiP2pManager manager;


    BroadcastReceiver broadcastReceiver;
    private WifiP2pManager mManager;
    private WifiP2pManager.Channel mChannel;
    private BRDirect mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(this, getMainLooper(), null);
        initView();
        initFilter();
        mReceiver = new BRDirect(mManager, mChannel, this);
        registerReceiver(mReceiver, intentFilter);




        mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
            }

            @Override
            public void onFailure(int reason) {


            }
        });



//        myRef = FirebaseDatabase.getInstance().getReference("Channels");

    }

    private void initFilter() {
        intentFilter = new IntentFilter();
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_DISCOVERY_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

    }

    private void initView() {
        searchPir.setOnClickListener(v -> {


        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        mReceiver = new BRDirect(mManager, mChannel, this);
        registerReceiver(mReceiver, intentFilter);


    }
//        users1.add(user1);
//        users1.add(new User("name 2", "email 2"));
//        users1.add(new User("name 3", "email 3"));
//        users1.add(new User("name 4", "email 4"));
//
//        messages1.add(new Message(111, user1, "test 1"));
//        messages1.add(new Message(111, user1, "test 44"));
//        messages1.add(new Message(111, user1, "test tetr1"));
//        messages1.add(new Message(111, user1, "test 1"));
//        messages1.add(new Message(111, user1, "test 1"));
//        messages1.add(new Message(111, user1, "test rtrt1"));
//        messages1.add(new Message(111, user1, "test 1"));
//
//
//        User user2 = new User("ffffffffff 1", "emaifffffffl 1");
//        users2.add(user2);
//        users2.add(new User("nddddame 2", "emaildsd 2"));
//        users2.add(new User("namdsfe 3", "email 3"));
//        users2.add(new User("nadddme 4", "email ddddd4"));
//
//        messages2.add(new Message(1114, user2, "teffffst 1"));
//        messages2.add(new Message(1411, user2, "teffst 44"));
//        messages2.add(new Message(111, user2, "test tetr1"));
//        messages2.add(new Message(1114, user2, "tesft 1"));
//        messages2.add(new Message(111, user2, "testff 1"));
//        messages2.add(new Message(1141, user2, "tesfft rtrt1"));
//        messages2.add(new Message(111, user2, "testfffff 1"));
////        myRef.setValue(new User("Bjob", "man"));
//
//    }
//
//    Channel channel = new Channel("test 1", users1, messages1);
//
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.create_chanal_1:
//                myRef.child(myRef.push().getKey()).setValue(channel);
//
//                break;
//            case R.id.create_chanal_2:
//                Channel channel2 = new Channel("test 2 chenel", users1, messages2);
//                myRef.child(myRef.push().getKey()).setValue(channel2);
//                break;
//            case R.id.create_user_1:
//                channel.addMessage(new Message(444, user1, "aded"));
//                break;
//            case R.id.create_user_2:
//                break;
//            case R.id.create_message_1:
//                break;
//            case R.id.create_message_2:
//                break;
//        }
//
//    }
}
