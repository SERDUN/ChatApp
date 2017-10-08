package com.example.dmitro.chatapp.screen.setting.firebase;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.dmitro.chatapp.R;
import com.example.dmitro.chatapp.data.model.firebase.ChannelKey;
import com.example.dmitro.chatapp.data.repository.ChatRepositoryManager;
import com.example.dmitro.chatapp.data.repository.Injection;
import com.example.dmitro.chatapp.screen.chat.ChatActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class ChannelFirebaseActivity extends AppCompatActivity implements ChannelFirebaseContract.View {
    private String LOG = "ChannelFirebaseActivity_log";
    public static final String KEY_CHANNEL = "key_channel";

    private ChannelFirebaseContract.Presenter presenter;
    private ChannelRecyclerAdapter channelRecyclerAdapter;

    @BindView(R.id.channelRV)
    public RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel_firebase);
        ButterKnife.bind(this);
        new ChannelFirebasePresenter(this, Injection.provideManager());
        initView();
        presenter.getChannels();

    }

    private void initView() {
        channelRecyclerAdapter = new ChannelRecyclerAdapter(new ArrayList<ChannelKey>() {
        }, event -> {
            Intent intent = new Intent(this, ChatActivity.class);
            intent.putExtra(KEY_CHANNEL, event.getKey());
            startActivity(intent);
        });
        recyclerView.setAdapter(channelRecyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void initPresenter(ChannelFirebaseContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showChannel(List<ChannelKey> channelKeys) {
        channelRecyclerAdapter.updateData((ArrayList<ChannelKey>) channelKeys);
    }
}
