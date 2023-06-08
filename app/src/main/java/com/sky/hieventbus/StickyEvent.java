package com.sky.hieventbus;

public class StickyEvent {
    private static String message;

    public StickyEvent(String message)
    {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
