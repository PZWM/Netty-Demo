package com.pzwm.netty.core.constant;

public enum MessageType {

    XML("XML"), JSON("JSON");

    private String type;

    private MessageType(String type) {
        this.type = type;
    }
}
