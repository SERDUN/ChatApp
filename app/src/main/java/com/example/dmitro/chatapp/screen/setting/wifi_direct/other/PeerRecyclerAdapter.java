package com.example.dmitro.chatapp.screen.setting.wifi_direct.other;

import android.net.wifi.p2p.WifiP2pDevice;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dmitro.chatapp.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dmitro on 08.10.17.
 */

public class PeerRecyclerAdapter extends RecyclerView.Adapter<PeerRecyclerAdapter.PeerHolder> {
    private ArrayList<WifiP2pDevice> devices;
    private PeerRecyclerListener peerRecyclerListener;

    public PeerRecyclerAdapter(ArrayList<WifiP2pDevice> devices, PeerRecyclerListener peerRecyclerListener) {
        this.devices = devices;
        this.peerRecyclerListener = peerRecyclerListener;
    }


    public void updateData(ArrayList<WifiP2pDevice> wifiP2pDevices) {
        if (wifiP2pDevices.size() != devices.size()) {
            this.devices = wifiP2pDevices;
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
        holder.bindView(devices.get(position));
    }

    @Override
    public int getItemCount() {
        return devices.size();
    }

    public class PeerHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.peerNameTV)
        public TextView peerNameTv;

        public PeerHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(v -> {
                peerRecyclerListener.call(devices.get(getAdapterPosition()));
            });
        }

        public void bindView(WifiP2pDevice device) {
            peerNameTv.setText(device.deviceName);
        }
    }
}
