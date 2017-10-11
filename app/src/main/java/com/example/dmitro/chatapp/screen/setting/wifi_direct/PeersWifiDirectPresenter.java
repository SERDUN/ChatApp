package com.example.dmitro.chatapp.screen.setting.wifi_direct;

/**
 * Created by dmitro on 09.10.17.
 */

public class PeersWifiDirectPresenter implements PeersWifiDirectContract.Presenter {
    private PeersWifiDirectContract.View view;

    public PeersWifiDirectPresenter(PeersWifiDirectContract.View view) {
        this.view = view;
    }

    @Override
    public void getPeers() {

    }
}
