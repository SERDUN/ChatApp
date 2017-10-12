package com.example.dmitro.chatapp.utils;

/**
 * Created by dmitro on 11.10.17.
 */

public interface Observable {

    void registerObserver(Observer o);

    void removeObserver(Observer o);

    <T> void notifyObserver(T t);
}
