package com.example.dmitro.chatapp.screen.setting.firebase;

import com.example.dmitro.chatapp.BasePresenter;
import com.example.dmitro.chatapp.BaseView;
import com.example.dmitro.chatapp.data.model.firebase.ChannelKey;
import com.example.dmitro.chatapp.screen.welcome.WelcomeContract;

import java.util.List;

/**
 * Created by dmitro on 08.10.17.
 */

public class ChannelFirebaseContract {
    interface View extends BaseView<Presenter> {
        public void showChannel(List<ChannelKey> channelKeys);
    }

    interface Presenter extends BasePresenter {
        void getChannels();
        void getUsersFromTheChannel();
    }
}
