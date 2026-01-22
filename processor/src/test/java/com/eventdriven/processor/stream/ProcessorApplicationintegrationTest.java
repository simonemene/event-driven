package com.eventdriven.processor.stream;

import com.eventdriven.processor.ProcessorApplicationTests;
import com.eventdriven.processor.dto.OrderDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import java.math.BigDecimal;

@Import(TestChannelBinderConfiguration.class)
public class ProcessorApplicationintegrationTest extends ProcessorApplicationTests {

    @Autowired
    private OutputDestination outputDestination;

    @Autowired
    private InputDestination inputDestination;

    @Test
    public void processor()
    {
        //given
        OrderDto orderDto = new OrderDto("apple",new BigDecimal("10.2"));
        Message<OrderDto> messageInput = MessageBuilder.withPayload(orderDto).build();
        //when
        inputDestination.send(messageInput,"order-topic");
        Message<byte[]> receive = outputDestination.receive(5000,"stock-topic");
        //then
        Assertions.assertNotNull(receive);
    }

}
