package com.eventdriven.source.controller;

import com.eventdriven.source.SourceApplicationTests;
import com.eventdriven.source.dto.OrderDto;
import com.eventdriven.source.properties.CloudConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.Message;
import org.springframework.test.context.TestPropertySource;

import java.io.IOException;
import java.math.BigDecimal;

@TestPropertySource(
        properties = {"spring.datasource.url=jdbc:h2:mem:dbtest",
        "spring.datasource.name=user",
        "spring.datasource.password="
        }
)
@Import(TestChannelBinderConfiguration.class)
public class StreamBridgeControllerIntegrationTest extends SourceApplicationTests {

    @Autowired
    private OutputDestination outputDestination;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private CloudConfig cloudConfig;

    @LocalServerPort
    private int localServerPort;


    @Test
    public void destinationStreamOrder() throws IOException {
        //given
        OrderDto orderDto =
                new OrderDto("ORDER-1",new BigDecimal("10.21"));
        //when
        testRestTemplate.postForEntity(
                "http://localhost:" + localServerPort + "/api/source",orderDto,Void.class
        );
        //then
        Message<byte[]> message = outputDestination.receive(5000,cloudConfig.nameBridge());
        ObjectMapper objectMapper = new ObjectMapper();
        OrderDto result = objectMapper.readValue(message.getPayload(),OrderDto.class);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result,orderDto);
    }


}
