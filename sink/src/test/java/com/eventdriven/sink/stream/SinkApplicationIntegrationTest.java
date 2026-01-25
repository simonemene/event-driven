package com.eventdriven.sink.stream;

import com.eventdriven.sink.SinkApplicationTests;
import com.eventdriven.sink.dto.OrderAvaiableDto;
import com.eventdriven.sink.dto.OrderDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;

@TestPropertySource(
        properties = {
                "spring.datasource.url=jdbc:h2:mem:testdb",
                "spring.datasource.username=sa",
                "spring.datasource.password=",
                "spring.datasource.driver-class-name=org.h2.Driver"
        }
)
@Import({TestChannelBinderConfiguration.class, SinkConfiguration.class})
public class SinkApplicationIntegrationTest extends SinkApplicationTests {

    @Autowired
    private JdbcClient jdbcClient;

    @Autowired
    private InputDestination inputDestination;

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
        //then
        String name = jdbcClient.sql("SELECT NAME FROM ORDER_AVAIABLE")
                        .query()
                        .singleValue().toString();
        Assertions.assertEquals("phone",dto.order().nameOrder());

    }


}
