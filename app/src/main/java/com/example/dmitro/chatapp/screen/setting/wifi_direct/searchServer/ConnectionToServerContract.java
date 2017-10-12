package com.example.dmitro.chatapp.screen.setting.wifi_direct.searchServer;

import com.example.dmitro.chatapp.BasePresenter;
import com.example.dmitro.chatapp.BaseView;

/**
 * Created by dmitro on 12.10.17.
 */

public class ConnectionToServerContract {
    public interface View extends BaseView<Presenter> {

        void connectionToServer(String ip, int port);
        void showMessage(int id);
    }

    interface Presenter extends BasePresenter {
        public String getWifiNetworkIP();

    }
}
