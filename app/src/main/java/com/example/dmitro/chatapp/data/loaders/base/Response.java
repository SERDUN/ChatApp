package com.example.dmitro.chatapp.data.loaders.base;

import android.content.Context;

/**
 * Created by dmitro on 30.09.17.
 */

public class Response {

    private Object answer;
    private RequestResult requestResult;

    public Response() {
        requestResult = RequestResult.ERROR;
    }

    public RequestResult getRequestResult() {
        return requestResult;
    }

    public Response setRequestResult(RequestResult requestResult) {
        this.requestResult = requestResult;
        return this;
    }

    public <T> T getTypedAnswer() {
        if (answer == null) {
            return null;
        }
        return (T) answer;
    }

    public Response setAnswer(Object answer) {
        this.answer = answer;
        return this;
    }

    public void save(Context context) {
    }
}
