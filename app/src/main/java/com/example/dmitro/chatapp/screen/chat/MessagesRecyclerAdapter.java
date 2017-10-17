package com.example.dmitro.chatapp.screen.chat;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dmitro.chatapp.R;
import com.example.dmitro.chatapp.data.model.wifiDirect.socket.data_object.Body;

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

        @BindView(R.id.bubble_layout_parent)
        LinearLayout parent_layout;

        public ChannelHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(v -> {
//                messagesRecyclerListener.call(messages.get(getAdapterPosition()));
            });
        }

        public void bindView(Body message) {

//
//            if (message.getAuthor().equals(MyUtils.WIFIDirect.getCurrentUser().getLogin())) {
//                layout.setBackgroundResource(R.drawable.bubble2);
//                parent_layout.setGravity(Gravity.RIGHT);
//            }
//            // If not mine then align to left
//            else {
//                layout.setBackgroundResource(R.drawable.bubble1);
//                parent_layout.setGravity(Gravity.LEFT);
//            }
//
//            if (message.getFile() != null) {
//                messageTextView.setVisibility(View.GONE);
//                imageIV.setVisibility(View.VISIBLE);
//                Bitmap bitmap = BitmapFactory.decodeByteArray(message.getFile(), 0, message.getFile().length);
//                imageIV.setImageBitmap(bitmap);
////                imageIV.setImageResource(R.mipmap.ic_launcher);
//            } else {
//                messageTextView.setVisibility(View.VISIBLE);
//                imageIV.setVisibility(View.GONE);
//            }

            switch (message.getType()) {
                case TEXT:
                    initViewText(message);
                    break;
            }

        }

        private void initViewText(Body message) {
            authorTextView.setText(message.getLogin());
            messageTextView.setText(new String(message.getBody()));
        }
    }
}