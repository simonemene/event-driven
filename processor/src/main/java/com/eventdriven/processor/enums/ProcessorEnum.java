package com.eventdriven.processor.enums;

import lombok.Getter;

@Getter
public enum ProcessorEnum {

    IN_STOCK("IN STOCK"),
    OUT_STOCK("OUT STOCK");

    private String value;

    ProcessorEnum(String value) {
        this.value = value;
    }
}
