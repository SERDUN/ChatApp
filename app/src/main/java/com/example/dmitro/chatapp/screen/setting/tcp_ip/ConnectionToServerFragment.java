package com.example.dmitro.chatapp.screen.setting.tcp_ip;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dmitro.chatapp.R;

/**
 * Created by dmitro on 10.10.17.
 */

public class ConnectionToServerFragment extends Fragment {
    public ConnectionToServerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_connection_to_server, container, false);
    }

}
