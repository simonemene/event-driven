package com.eventdriven.source.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("source")
public record CloudConfig(String nameBridge) {
}
