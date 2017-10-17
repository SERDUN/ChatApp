package com.example.dmitro.chatapp.screen.chat.setting_connection;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ToggleButton;

import com.example.dmitro.chatapp.ChatApp;
import com.example.dmitro.chatapp.DetectActivity;
import com.example.dmitro.chatapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingCurrentConnectionActivity extends DetectActivity {


    @BindView(R.id.continueWorkTG)
    ToggleButton continueWorkTG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_current_connection);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        continueWorkTG.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            SharedPreferences sharedPref = ChatApp.getInstance().getSharedPreferences(ChatApp.getInstance().getString(R.string.setting_client),
                    Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean(ChatApp.getInstance().getString(R.string.continue_work_client_connection), isChecked);
            editor.apply();
        });
    }
}
