package com.example.dmitro.chatapp.screen.setting.wifi_direct.searchServer;


import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;

import com.example.dmitro.chatapp.ChatApp;

import static android.content.Context.WIFI_SERVICE;

/**
 * Created by dmitro on 12.10.17.
 */

public class ConnectionToServerPresenter implements ConnectionToServerContract.Presenter {
    private ConnectionToServerContract.View view;

    public ConnectionToServerPresenter(ConnectionToServerContract.View view) {
        this.view = view;
        view.initPresenter(this);
    }

    @Override
    public String getWifiNetworkIP() {
        WifiManager wifiMgr = (WifiManager) ChatApp.getInstance().getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
        int ip = wifiInfo.getIpAddress();
        return Formatter.formatIpAddress(ip);
    }
}
