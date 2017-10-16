package com.example.dmitro.chatapp.screen.setting.tcp_ip;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.example.dmitro.chatapp.R;
import com.example.dmitro.chatapp.screen.setting.tcp_ip.create_server.CreateServerFragment;
import com.example.dmitro.chatapp.screen.setting.tcp_ip.create_server.CreateServerPresenter;
import com.example.dmitro.chatapp.screen.setting.tcp_ip.search_server.ConnectionToServerFragment;
import com.example.dmitro.chatapp.screen.setting.tcp_ip.search_server.ConnectionToServerPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TCPIPSettingActivity extends AppCompatActivity {
    @BindView(R.id.tabs)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewPager;

    private CreateServerFragment createServerFragment;
    private ConnectionToServerFragment connectionToServerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tcpipsetting);
        ButterKnife.bind(this);

        tabLayout.setupWithViewPager(viewPager);
        createServerFragment = CreateServerFragment.getInstance();
        connectionToServerFragment = ConnectionToServerFragment.getInstance();
        new CreateServerPresenter(createServerFragment);
        new ConnectionToServerPresenter(connectionToServerFragment);
        setupViewPager(viewPager);

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(createServerFragment, getString(R.string.create_server));
        adapter.addFragment(connectionToServerFragment, getString(R.string.connect_to_server));

        viewPager.setAdapter(adapter);
    }
}
