package com.eventdriven.testcontainer.kafka;

import com.eventdriven.processor.dto.OrderAvailableDto;
import com.eventdriven.processor.dto.OrderDto;
import com.eventdriven.sink.repository.OrderAvaiableRepository;
import com.eventdriven.source.savemessage.MessageRespository;
import com.eventdriven.testcontainer.TestcontainerApplicationTests;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.testcontainers.shaded.org.awaitility.Awaitility;

import java.math.BigDecimal;
import java.time.Duration;

@SpringBootTest(classes = KafkaTestContainersIntegrationTest.ContextApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class KafkaTestContainersIntegrationTest extends TestcontainerApplicationTests {

    @Autowired
    private TestRestTemplate template;

    @Autowired
    private JdbcClient jdbcClient;

    @Autowired
    private MessageRespository messageRespository;

    @Autowired
    private OrderAvaiableRepository avaiableRepository;


    @Test
    public void kafka()
    {

        //given
        com.eventdriven.source.dto.OrderDto orderDto = new com.eventdriven.source.dto.OrderDto("phone",new BigDecimal("12.21"));
        //when
        template.postForEntity("/api/source",orderDto,Void.class);
        //then
        Awaitility.await()
                .atMost(Duration.ofSeconds(30))
                .pollInterval(Duration.ofMillis(200))
                .untilAsserted(() -> {
                    long count = messageRespository.count();

                    Assertions.assertThat(count).isGreaterThan(0L);
                });
        Awaitility.await().atMost(Duration.ofSeconds(30)).pollInterval(Duration.ofMillis(200))
                .untilAsserted(()-> {
                    String idEvent = messageRespository.findAll().get(0).getEventId();

                    OrderAvailableDto result =
                            jdbcClient.sql("SELECT NAME,COST,STATUS_STOCK,ID_EVENT FROM ORDER_AVAILABLE WHERE ID_EVENT = ?")
                            .param(1, idEvent)
                            .query((rs, rowNum) ->
                            {
                                return new OrderAvailableDto(
                                        new OrderDto(rs.getString(4), rs.getString(1), rs.getBigDecimal(2)),
                                        rs.getString(3));

                            }).single();
                    Assertions.assertThat(result.order().name()).isEqualTo(orderDto.name());
                    Assertions.assertThat(result.order().cost()).isEqualTo(orderDto.cost());
                }
                );

    }


    @EnableJpaAuditing
    @EnableScheduling
    @Import(FlywayAutoConfiguration.FlywayConfiguration.class)
    @SpringBootApplication(
            scanBasePackages =
                    {
                            "com.eventdriven.source",
                            "com.eventdriven.processor",
                            "com.eventdriven.sink"
                    }
    )
    public static class ContextApplication
    {

    }
}
