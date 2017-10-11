package com.example.dmitro.chatapp.data.loaders.base;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.io.IOException;

/**
 * Created by dmitro on 30.09.17.
 */

public abstract class BaseLoader extends AsyncTaskLoader<Response> {
    public BaseLoader(Context context) {
        super(context);
    }

    @Override
    public Response loadInBackground() {
        return null;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    protected Response onLoadInBackground() {

        try {
            Response response = apiCall();

            if (response.getRequestResult() == RequestResult.SUCCESS) {
                response.save(getContext());
                onSuccess();
            } else {
                onError();
            }
            return response;
        } catch (IOException e) {
            onError();
            return new Response();
        }

    }


    protected void onSuccess() {
    }

    protected void onError() {
    }

    protected abstract Response apiCall() throws IOException;
}
