package com.example.dmitro.chatapp.screen.setting.tcp_ip;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.dmitro.chatapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TCPIPSettingActivity extends AppCompatActivity {
    @BindView(R.id.tabs)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tcpipsetting);
        ButterKnife.bind(this);
        setupViewPager(viewPager);

        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new CreateServerFragment(), getString(R.string.create_server));
        adapter.addFragment(new ConnectionToServerFragment(),getString(R.string.connect_to_server));

        viewPager.setAdapter(adapter);
    }
}
