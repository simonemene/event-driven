package com.eventdriven.processor.stream;

import com.eventdriven.processor.ProcessorApplicationTests;
import com.eventdriven.processor.dto.OrderAvailableDto;
import com.eventdriven.processor.dto.OrderDto;
import com.eventdriven.processor.enums.ProcessorEnum;
import com.eventdriven.processor.service.IRandomAvailableService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.io.IOException;
import java.math.BigDecimal;

@Import(TestChannelBinderConfiguration.class)
public class ProcessorApplicationintegrationTest extends ProcessorApplicationTests {

    @Autowired
    private OutputDestination outputDestination;

    @Autowired
    private InputDestination inputDestination;

    @MockitoBean
    private IRandomAvailableService service;

    AutoCloseable autoCloseable;

    @BeforeEach
    public void init()
    {
        autoCloseable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void close() throws Exception {
        autoCloseable.close();
    }

    @Test
    public void processor() throws IOException {
        //given
        Mockito.when(service.isAvaiable()).thenReturn(ProcessorEnum.IN_STOCK.getValue());
        OrderDto orderDto = new OrderDto("12a","apple",new BigDecimal("10.2"));
        OrderAvailableDto check = new OrderAvailableDto(orderDto,ProcessorEnum.IN_STOCK.getValue());
        Message<OrderDto> messageInput = MessageBuilder.withPayload(orderDto).build();
        //when
        inputDestination.send(messageInput,"order-topic");
        Message<byte[]> receive = outputDestination.receive(5000,"stock-topic");
        //then
        ObjectMapper objectMapper = new ObjectMapper();
        OrderAvailableDto result = objectMapper.readValue(receive.getPayload(), OrderAvailableDto.class);
        Assertions.assertNotNull(receive);
        Assertions.assertEquals(result,check);
    }

}
