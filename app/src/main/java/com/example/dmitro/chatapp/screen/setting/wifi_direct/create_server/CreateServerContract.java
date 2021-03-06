package com.example.dmitro.chatapp.screen.setting.wifi_direct.create_server;

import com.example.dmitro.chatapp.BasePresenter;
import com.example.dmitro.chatapp.BaseView;

/**
 * Created by dmitro on 11.10.17.
 */

public class CreateServerContract {
    public interface View extends BaseView<Presenter> {

        void startServer();
        void showMessage(int id);
        void showDialogueActivity();
    }

    interface Presenter extends BasePresenter {
        void startServer();
        void startDialogue();


    }
}
