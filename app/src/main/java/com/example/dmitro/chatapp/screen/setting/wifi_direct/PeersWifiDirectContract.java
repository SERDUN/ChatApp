package com.example.dmitro.chatapp.screen.setting.wifi_direct;

import android.net.wifi.p2p.WifiP2pDevice;

import com.example.dmitro.chatapp.BasePresenter;
import com.example.dmitro.chatapp.BaseView;
import com.example.dmitro.chatapp.screen.welcome.WelcomeContract;

import java.util.Collection;

/**
 * Created by dmitro on 09.10.17.
 */

public class PeersWifiDirectContract {

    interface View extends BaseView<Presenter> {
        void showPeers(Collection<WifiP2pDevice> devices);

        void showMessage(int id);
    }

    interface Presenter extends BasePresenter {
        public void getPeers();
    }
}
