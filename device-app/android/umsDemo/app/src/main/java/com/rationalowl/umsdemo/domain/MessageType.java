package com.rationalowl.umsdemo.domain;

import com.fasterxml.jackson.annotation.JsonValue;

public enum MessageType {
    NORMAL(1),
    EMERGENCY(2);

    private final int value;

    MessageType(int value) {
        this.value = value;
    }

    @JsonValue
    public int getValue() {
        return this.value;
    }

    public static MessageType forValue(int value) {
        for (MessageType element : values()) {
            if (element.value == value) {
                return element;
            }
        }

        return null;
    }
}
