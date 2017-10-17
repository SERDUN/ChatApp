package com.example.dmitro.chatapp;

import android.support.v7.app.AppCompatActivity;


/**
 * Created by dmitro on 17.10.17.
 */

public class DetectActivity extends AppCompatActivity {
    @Override
    protected void onResume() {
        super.onResume();
        ((ChatApp) getApplication()).setIsAppRunning(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ((ChatApp) getApplication()).setIsAppRunning(false);
    }
}
