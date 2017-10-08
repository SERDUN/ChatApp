package com.example.dmitro.chatapp.screen.setting.firebase;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dmitro.chatapp.R;
import com.example.dmitro.chatapp.data.model.firebase.ChannelKey;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dmitro on 08.10.17.
 */

public class ChannelRecyclerAdapter extends RecyclerView.Adapter<ChannelRecyclerAdapter.ChannelHolder> {
    private ArrayList<ChannelKey> channelKeys;
    private ChannelRecyclerListener channelRecyclerListener;

    public ChannelRecyclerAdapter(ArrayList<ChannelKey> channelKeys, ChannelRecyclerListener channelRecyclerListener) {
        this.channelKeys = channelKeys;
        this.channelRecyclerListener = channelRecyclerListener;
    }


    public void updateData(ArrayList<ChannelKey> channelKeys) {
        this.channelKeys = channelKeys;
        notifyDataSetChanged();

    }

    @Override
    public ChannelHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.channels_item, parent, false);
        return new ChannelHolder(view);
    }

    @Override
    public void onBindViewHolder(ChannelHolder holder, int position) {
        holder.bindView(channelKeys.get(position));
    }

    @Override
    public int getItemCount() {
        return channelKeys.size();
    }

    public class ChannelHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.nameChannelTV)
        public TextView nameChannel;

        public ChannelHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(v->{
                channelRecyclerListener.call(channelKeys.get(getAdapterPosition()));
            });
        }

        public void bindView(ChannelKey channelKey) {
            nameChannel.setText(channelKey.getNameChannel());
        }
    }
}
