package com.sky.hieventbus;

public class MessageEvent {
    private static String message;

    public MessageEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
