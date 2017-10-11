package com.example.dmitro.chatapp.data.loaders;

import android.content.Context;

import com.example.dmitro.chatapp.data.loaders.base.BaseLoader;
import com.example.dmitro.chatapp.data.loaders.base.Response;

import java.io.IOException;

/**
 * Created by dmitro on 11.10.17.
 */

public class WifiDirectMessageLoader extends BaseLoader {

    public WifiDirectMessageLoader(Context context) {
        super(context);
    }

    @Override
    protected Response apiCall() throws IOException {
        return null;
    }
}
