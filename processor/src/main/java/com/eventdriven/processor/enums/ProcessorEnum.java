package com.eventdriven.processor.enums;

import lombok.Getter;

@Getter
public enum ProcessorEnum {

    IN_STOCK("IN_STOCK"),
    OUT_STOCK("OUT_STOCK");

    private String value;

    ProcessorEnum(String value) {
        this.value = value;
    }
}
