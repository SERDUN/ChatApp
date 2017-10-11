package com.example.dmitro.chatapp.connection;

/**
 * Created by dmitro on 08.10.17.
 */

public enum TypeConnection {

    CONNECT_TO_SERVER_TOMCAT("Tomcat connection"),

    CONNECT_TO_SERVER_FIREBASE("Firebase connection"),

    CONNECT_TO_BLUETOOTH("Bluetooth connection"),

    CONNECT_TO_WIFI_DIRECT("WifiDirect connection"),

    CONNECT_TO_TCP_IP("TCP_IP connection");

    private final String name;

    private TypeConnection(String name) {
        this.name = name;
    }

    public boolean equalsName(String otherName) {
        return name.equals(otherName);
    }

    public String toString() {
        return this.name;
    }
}
