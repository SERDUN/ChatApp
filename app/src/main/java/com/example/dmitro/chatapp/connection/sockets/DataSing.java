package com.example.dmitro.chatapp.connection.sockets;

import com.example.dmitro.chatapp.data.model.wifiDirect.socket.transport_object.Request;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dmitro on 10.10.17.
 */

public class DataSing {

    private List<Request> history;

    private static final DataSing ourInstance = new DataSing();



    public static DataSing getInstance() {
        return ourInstance;
    }

    private DataSing() {
        history= new ArrayList<>();
    }

    public void addRecords(Request request){
        history.add(request);
    }
}
