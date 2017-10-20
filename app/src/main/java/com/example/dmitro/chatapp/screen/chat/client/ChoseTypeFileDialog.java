package com.example.dmitro.chatapp.screen.chat.client;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dmitro.chatapp.R;

/**
 * Created by dmitro on 18.10.17.
 */

public class ChoseTypeFileDialog extends DialogFragment implements View.OnClickListener {
    private ClickableListener clickableListener;
    final String LOG_TAG = "myLogs";


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().setTitle("Title!");
        View v = inflater.inflate(R.layout.chose_type_file, null);
        v.findViewById(R.id.audioBT).setOnClickListener(this);
        v.findViewById(R.id.photoBT).setOnClickListener(this);
        return v;
    }

    public void onClick(View v) {
        if (clickableListener != null) clickableListener.onClick(v.getId());
        dismiss();
    }

    public void setClickableListener(ClickableListener clickableListener) {
        this.clickableListener = clickableListener;
    }

    public interface ClickableListener {
        public void onClick(int id);
    }
}