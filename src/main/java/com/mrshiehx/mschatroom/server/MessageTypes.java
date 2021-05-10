package com.mrshiehx.mschatroom.server;

public enum MessageTypes {
    TEXT(0),
    PICTURE(1),
    FILE(2);

    public final int code;

    MessageTypes(int code) {
        this.code = code;
    }
}
