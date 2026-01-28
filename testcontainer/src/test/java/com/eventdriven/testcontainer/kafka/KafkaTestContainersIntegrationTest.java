package com.eventdriven.testcontainer.kafka;

import com.eventdriven.processor.ProcessorApplication;
import com.eventdriven.processor.dto.OrderAvailableDto;
import com.eventdriven.processor.dto.OrderDto;
import com.eventdriven.sink.SinkApplication;
import com.eventdriven.source.SourceApplication;
import com.eventdriven.testcontainer.TestcontainerApplicationTests;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.simple.JdbcClient;

import java.math.BigDecimal;

public class KafkaTestContainersIntegrationTest extends TestcontainerApplicationTests {

    @Autowired
    private TestRestTemplate template;

    @Autowired
    private JdbcClient jdbcClient;


    @Test
    public void kafka()
    {

        //given
        com.eventdriven.source.dto.OrderDto orderDto = new com.eventdriven.source.dto.OrderDto("phone",new BigDecimal("12.21"));
        //when
        template.postForEntity("/api/source",orderDto,Void.class);
        //then
        String idEvent = jdbcClient.sql("SELECT ID_EVENT FROM MESSAGE")
                .query(String.class)
                .single();
        OrderAvailableDto result = jdbcClient.sql("SELECT NAME,COST,STATUS_STOCK,ID_EVENT FROM ORDER_AVAILABLE WHERE ID_EVENT = ?")
                .param(1,idEvent)
                .query((rs,rowNum)->
                {
                    return new OrderAvailableDto(
                            new OrderDto(rs.getString(4),rs.getString(1),rs.getBigDecimal(2)),
                                    rs.getString(3));

                }).single();
        Assertions.assertThat(result.order().name()).isEqualTo(orderDto.name());
        Assertions.assertThat(result.order().cost()).isEqualTo(orderDto.cost());
    }


    @EnableAutoConfiguration
    @Import({
            SourceApplication.class, SinkApplication.class, ProcessorApplication.class
    })
    public static class application
    {

    }
}
