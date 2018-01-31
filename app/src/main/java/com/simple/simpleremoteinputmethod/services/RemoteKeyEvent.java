package com.simple.simpleremoteinputmethod.services;

/**
 * Created by mrsimple on 30/1/2018.
 */

public class RemoteKeyEvent {
    public static final int HOME_CODE = 3 ;
    public static final int CLEAR_CODE = 68 ;

    public int keyCode;

    private RemoteKeyEvent(int mKeyCode) {
        this.keyCode = mKeyCode;
    }

    public static RemoteKeyEvent create(int keyCode) {
        return new RemoteKeyEvent(keyCode) ;
    }

    @Override
    public String toString() {
        return "RemoteInputEvent{" + "keyCode=" + keyCode + '}';
    }
}
