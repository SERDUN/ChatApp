package com.example.dmitro.chatapp.screen.chat;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dmitro.chatapp.ChatApp;
import com.example.dmitro.chatapp.R;
import com.example.dmitro.chatapp.data.model.wifiDirect.socket.data_object.Body;
import com.example.dmitro.chatapp.data.model.wifiDirect.socket.data_object.Type;
import com.example.dmitro.chatapp.utils.MyUtils;
import com.example.dmitro.chatapp.utils.StorageUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dmitro on 15.10.17.
 */

public class MessagesRecyclerAdapter extends RecyclerView.Adapter<MessagesRecyclerAdapter.ChannelHolder> {
    private ArrayList<Body> messages;
    private MessagesRecyclerListener messagesRecyclerListener;

    public MessagesRecyclerAdapter(ArrayList<Body> messages, MessagesRecyclerListener messagesRecyclerListener) {
        this.messages = messages;
        this.messagesRecyclerListener = messagesRecyclerListener;
    }


    public void updateData(ArrayList<Body> messages) {
        this.messages = messages;
        notifyDataSetChanged();

    }

    public void addMessage(Body message) {
        messages.add(message);
        notifyDataSetChanged();
    }

    ;

    @Override
    public ChannelHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.message_item, parent, false);
        return new ChannelHolder(view);
    }

    @Override
    public void onBindViewHolder(ChannelHolder holder, int position) {
        holder.bindView(messages.get(position));
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class ChannelHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.authorTV)
        public TextView authorTextView;

        @BindView(R.id.messageTV)
        public TextView messageTextView;
        @BindView(R.id.imageImageView)
        public ImageView imageIV;

        @BindView(R.id.bubble_layout)
        LinearLayout layout;

        @BindView(R.id.startAudioBT)
        public Button startAudioBT;

        @BindView(R.id.stopAudioBT)
        public Button stopAudioBT;

        @BindView(R.id.pauseAudioBT)
        public Button pauseAudioBT;

        @BindView(R.id.bubble_layout_parent)
        LinearLayout parent_layout;

        @BindView(R.id.playerLL)
        LinearLayout playerLL;

        private MediaPlayer mediaPlayer;


        public ChannelHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mediaPlayer = new MediaPlayer();

            itemView.setOnClickListener(v -> {
//                messagesRecyclerListener.call(messages.get(getAdapterPosition()));
            });
        }

        public void bindView(Body message) {

            switch (message.getType()) {
                case TEXT:
                    initViewText(message);
                    break;
                case PHOTO:
                    initViewImage(message);
                    break;
                case URI_AUDIO:
                    initViewAudio(message);
                    break;
            }

        }

        private void initViewAudio(Body message) {
            setVisibleContent(Type.AUDIO);
            View.OnClickListener onClickListener = view -> {
                switch (view.getId()) {
                    case R.id.pauseAudioBT:
                        if (mediaPlayer.isPlaying())
                            mediaPlayer.pause();
                        break;
                    case R.id.startAudioBT:
                        if (!mediaPlayer.isPlaying())
                            mediaPlayer.start();
                        break;
                    case R.id.stopAudioBT:
                        mediaPlayer.stop();
                        break;

                }
            };

            startAudioBT.setOnClickListener(view -> {
                try {
                    mediaPlayer.setDataSource(new String(message.getBody()));
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                    startAudioBT.setOnClickListener(onClickListener);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            });


            stopAudioBT.setOnClickListener(onClickListener);
            pauseAudioBT.setOnClickListener(onClickListener);
        }


        private void initViewImage(Body message) {
            setVisibleContent(Type.PHOTO);
            Bitmap bitmap = BitmapFactory.decodeByteArray(message.getBody(), 0, message.getBody().length);
            imageIV.setImageBitmap(bitmap);
        }

        private void initViewText(Body message) {
            setVisibleContent(Type.TEXT);
            if (message.getLogin().equals(MyUtils.WIFIDirect.getCurrentUser().getLogin())) {
                layout.setBackgroundResource(R.drawable.bubble2);
                parent_layout.setGravity(Gravity.RIGHT);
            } else {
                layout.setBackgroundResource(R.drawable.bubble1);
                parent_layout.setGravity(Gravity.LEFT);
            }
            messageTextView.setText(new String(message.getBody()));
        }

        public void setVisibleContent(Type type) {
            switch (type) {
                case PHOTO:
                    playerLL.setVisibility(View.GONE);
                    imageIV.setVisibility(View.VISIBLE);
                    messageTextView.setVisibility(View.GONE);
                    break;
                case TEXT:
                    playerLL.setVisibility(View.GONE);
                    imageIV.setVisibility(View.GONE);
                    messageTextView.setVisibility(View.VISIBLE);
                    break;
                case AUDIO:
                    playerLL.setVisibility(View.VISIBLE);
                    imageIV.setVisibility(View.GONE);
                    messageTextView.setVisibility(View.GONE);
                    break;
            }
        }

    }


}