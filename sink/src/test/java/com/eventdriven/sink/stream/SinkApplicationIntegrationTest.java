package com.eventdriven.sink.stream;

import com.eventdriven.sink.SinkApplicationTests;
import com.eventdriven.sink.dto.OrderAvaiableDto;
import com.eventdriven.sink.dto.OrderDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import java.math.BigDecimal;

@Import(TestChannelBinderConfiguration.class)
public class SinkApplicationIntegrationTest extends SinkApplicationTests {

    @Autowired
    private InputDestination inputDestination;

    @Autowired
    private OutputDestination outputDestination;

    @Autowired
    private JdbcClient jdbcClient;

    @Test
    public void readAndSave()
    {
        //given
        OrderDto order =
                new OrderDto("phone",new BigDecimal("12.12"));
        OrderAvaiableDto dto =
                new OrderAvaiableDto(order,"IN_STOCK");
        //when
        inputDestination.send(MessageBuilder.withPayload(dto).build(),
                        "stock-topic");
        Message<byte[]> outMessage = outputDestination
                .receive(5000,"stock-topic");
        //then
        Assertions.assertNotNull(outMessage.getPayload());

    }


}
