package com.example.dmitro.chatapp.screen.welcome;

import android.content.SharedPreferences;
import android.util.Log;

import com.example.dmitro.chatapp.ChatApp;
import com.example.dmitro.chatapp.R;
import com.example.dmitro.chatapp.connection.TypeConnection;
import com.example.dmitro.chatapp.utils.MyUtils;

/**
 * Created by dmitro on 08.10.17.
 */

public class WelcomePresenter implements WelcomeContract.Presenter {
    private WelcomeContract.View view;
    private SharedPreferences sharedPreferences;
    private final String TAG = "test_log";

    public WelcomePresenter(WelcomeContract.View view, SharedPreferences sharedPreferences) {
        this.view = view;
        this.sharedPreferences = sharedPreferences;
        view.initPresenter(this);

    }

    @Override
    public void saveName(String name) {
        if (!name.isEmpty()) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(ChatApp.getInstance().getString(R.string.user_name), name);
            editor.apply();
        }
    }

    @Override
    public void saveTypeConnection(String type) {
        Log.d(TAG, "saveTypeConnection: " + type);
        Log.d(TAG, "saveTypeConnection: " + TypeConnection.CONNECT_TO_SERVER_FIREBASE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(ChatApp.getInstance().getString(R.string.type_connection), type);
        editor.apply();

    }

    @Override
    public void openNextActivity() {
        if (view.isFieldNameEmpty()) {
            view.showMessage(R.string.field_name_is_empty);
        } else {
            switch (MyUtils.getCurrentTypeCoonection()) {
                case CONNECT_TO_SERVER_FIREBASE:
                    view.openFirebaseSetting();
                    break;
                case CONNECT_TO_SERVER_TOMCAT:
                    view.openConnectTomcatSetting();
                    break;
                case CONNECT_TO_WIFI_DIRECT:
                    view.openWifiDirectSetting();
                    break;
                case CONNECT_TO_TCP_IP:
                    view.openTCPIPSetting();


                    //// TODO: 08.10.17 Please complete later with other options
            }

        }

    }
}
