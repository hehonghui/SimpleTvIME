package com.simple.simpleremoteinputmethod.services;

/**
 * Created by mrsimple on 30/1/2018.
 */

public class RemoteInputEvent {

    public String text;

    public RemoteInputEvent(String text) {
        this.text = text;
    }

    public static RemoteInputEvent create(String text) {
        return new RemoteInputEvent(text) ;
    }

    @Override
    public String toString() {
        return "RemoteInputEvent{" + "text='" + text + '\'' + '}';
    }
}
