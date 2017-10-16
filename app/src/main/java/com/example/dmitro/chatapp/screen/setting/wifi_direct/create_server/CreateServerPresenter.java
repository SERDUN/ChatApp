package com.example.dmitro.chatapp.screen.setting.wifi_direct.create_server;

/**
 * Created by dmitro on 11.10.17.
 */

public class CreateServerPresenter implements CreateServerContract.Presenter {
    public CreateServerContract.View view;

    public CreateServerPresenter(CreateServerContract.View view) {
        this.view = view;
        view.initPresenter(this);
    }


    @Override
    public void startServer() {
        view.startServer();
    }

    @Override
    public void startDialogue() {
        view.showDialogueActivity();
    }
}
