package com.example.dmitro.chatapp.screen.setting.wifi_direct.create_server;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dmitro.chatapp.R;
import com.example.dmitro.chatapp.data.model.wifiDirect.User;
import com.example.dmitro.chatapp.screen.setting.wifi_direct.other.PeerRecyclerListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dmitro on 08.10.17.
 */

public class ConnectedUsersRecyclerAdapter extends RecyclerView.Adapter<ConnectedUsersRecyclerAdapter.PeerHolder> {
    private ArrayList<User> users;
    private PeerRecyclerListener peerRecyclerListener;

    public ConnectedUsersRecyclerAdapter(ArrayList<User> users, PeerRecyclerListener peerRecyclerListener) {
        this.users = users;
        this.peerRecyclerListener = peerRecyclerListener;
    }


    public void updateData(ArrayList<User> users) {
        if (users.size() != this.users.size()) {
            this.users = users;
            notifyDataSetChanged();
        }

    }

    @Override
    public PeerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.peer_item, parent, false);
        return new PeerHolder(view);
    }

    @Override
    public void onBindViewHolder(PeerHolder holder, int position) {
        holder.bindView(users.get(position));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class PeerHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.peerNameTV)
        public TextView peerNameTv;

        public PeerHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
//            itemView.setOnClickListener(v -> {
//                peerRecyclerListener.call(users.get(getAdapterPosition()));
//            });
        }

        public void bindView(User user) {
            peerNameTv.setText(user.getLogin());
        }
    }
}
