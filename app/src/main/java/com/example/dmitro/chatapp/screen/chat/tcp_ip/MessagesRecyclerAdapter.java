package com.example.dmitro.chatapp.screen.chat.tcp_ip;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dmitro.chatapp.R;
import com.example.dmitro.chatapp.data.model.wifiDirect.Message;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
/**
 * Created by dmitro on 15.10.17.
 */

public class MessagesRecyclerAdapter extends RecyclerView.Adapter<MessagesRecyclerAdapter.ChannelHolder> {
    private ArrayList<Message> messages;
    private MessagesRecyclerListener messagesRecyclerListener;

    public MessagesRecyclerAdapter(ArrayList<Message> messages, MessagesRecyclerListener messagesRecyclerListener) {
        this.messages = messages;
        this.messagesRecyclerListener = messagesRecyclerListener;
    }


    public void updateData(ArrayList<Message> messages) {
        this.messages = messages;
        notifyDataSetChanged();

    }

    public void addMessage(Message message) {
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

        public ChannelHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(v -> {
                messagesRecyclerListener.call(messages.get(getAdapterPosition()));
            });
        }

        public void bindView(Message FMessage) {
            authorTextView.setText(FMessage.getAuthor());
            messageTextView.setText(FMessage.getMessage());
        }
    }
}