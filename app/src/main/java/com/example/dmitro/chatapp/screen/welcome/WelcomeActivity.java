package com.example.dmitro.chatapp.screen.welcome;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.dmitro.chatapp.R;
import com.example.dmitro.chatapp.screen.setting.tcp_ip.TCPIPSettingActivity;
import com.example.dmitro.chatapp.screen.setting.wifi_direct.PeersWifiDirectActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WelcomeActivity extends AppCompatActivity implements WelcomeContract.View {

    @BindView(R.id.typeConnectionSpinner)
    public Spinner typeConnectionSpinner;

    @BindView(R.id.button)
    public Button applyButton;

    @BindView(R.id.nameEditText)
    public EditText nameEditText;

    @BindView(R.id.databaseTestWrite)
    Button databaseButtonWrite;

    @BindView(R.id.databaseTestRead)
    Button databaseButtonRead;

    private WelcomeContract.Presenter presenter;

    public String TAG = "database_log";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);
        new WelcomePresenter(this, getSharedPreferences(getString(R.string.connection_conf), Context.MODE_PRIVATE));
        initView();






        databaseButtonWrite.setOnClickListener(v -> {
//            Message message = new Message("test", "message", System.currentTimeMillis());
//            getContentResolver().insert(ContractClass.Messages.CONTENT_URI, createContentValues(message));


        });

        databaseButtonRead.setOnClickListener(v -> {


        });


    }





    private void initView() {
        final String[] arrayTypeConnection = getResources().getStringArray(R.array.type_connection);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, arrayTypeConnection);
        typeConnectionSpinner.setAdapter(adapter);
        typeConnectionSpinner.setPrompt(getResources().getString(R.string.type_connection));
        typeConnectionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(WelcomeActivity.this, "position: " + position, Toast.LENGTH_SHORT).show();
                presenter.saveTypeConnection(arrayTypeConnection[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        applyButton.setOnClickListener(v -> {
            presenter.saveName(nameEditText.getText().toString());
            presenter.openNextActivity();
        });


    }

    @Override
    public void initPresenter(WelcomeContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void openFirebaseSetting() {

    }

    @Override
    public void openConnectTomcatSetting() {

    }

    @Override
    public void openWifiDirectSetting() {
        Intent intent = new Intent(this, PeersWifiDirectActivity.class);
        startActivity(intent);
    }

    @Override
    public void openTCPIPSetting() {
        Intent intent = new Intent(this, TCPIPSettingActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean isFieldNameEmpty() {
        return nameEditText.getText().toString().isEmpty();
    }

    @Override
    public void showMessage(int id) {
        Toast.makeText(this, getString(id), Toast.LENGTH_SHORT).show();

    }
}
