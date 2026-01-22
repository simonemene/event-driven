package com.eventdriven.source.configuration;

import com.eventdriven.source.dto.OrderDto;
import com.eventdriven.source.properties.CloudConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Configuration;


@RequiredArgsConstructor
@Configuration
public class StreamConfiguration {

    private final StreamBridge streamBridge;

    private final CloudConfig cloudConfig;

    public void sendOrder(OrderDto orderDto) {
        streamBridge.send(cloudConfig.nameBridge(),orderDto);
    }
}
