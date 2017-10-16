package com.example.dmitro.chatapp.screen.setting.wifi_direct.other;

import android.net.wifi.p2p.WifiP2pDevice;

/**
 * Created by dmitro on 08.10.17.
 */

public interface PeerRecyclerListener {
    public void call(WifiP2pDevice channelKey);
}
