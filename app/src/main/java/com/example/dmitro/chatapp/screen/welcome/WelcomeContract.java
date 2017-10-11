package com.example.dmitro.chatapp.screen.welcome;

import com.example.dmitro.chatapp.BasePresenter;
import com.example.dmitro.chatapp.BaseView;

/**
 * Created by dmitro on 08.10.17.
 */

public class WelcomeContract {


    interface View extends BaseView<Presenter> {
        void openFirebaseSetting();

        void openConnectTomcatSetting();

        void openWifiDirectSetting();

        void openTCPIPSetting();

        boolean isFieldNameEmpty();

        void showMessage(int id);
    }

    interface Presenter extends BasePresenter {
        void saveName(String name);

        void saveTypeConnection(String string);

        void openNextActivity();


    }
}
