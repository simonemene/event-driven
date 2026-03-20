package com.eventdriven.processor.kafkafail;

import com.eventdriven.processor.ProcessorApplicationTests;
import com.eventdriven.processor.dto.OrderDto;
import com.eventdriven.processor.service.IRandomAvailableService;
import org.apache.kafka.clients.consumer.Consumer;
import org.assertj.core.api.Assertions;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.TimeUnit;

@ActiveProfiles("test")
@EmbeddedKafka(
        partitions = 1,
        topics = {"order-topic","order-topic.dlq","stock-topic","stock-topic.dlq"}
)
@SpringBootTest(
        properties = {
                "spring.kafka.producer.key-serializer=org.apache.kafka.common.serialization.StringSerializer",
                "spring.kafka.producer.key-serializer=org.apache.kafka.common.serialization.StringSerializer",
                "spring.kafka.producer.value-serializer=org.springframework.kafka.support.serializer.JsonSerializer",
                "spring.kafka.consumer.key-deserializer=org.apache.kafka.common.serialization.StringDeserializer",
                "spring.cloud.stream.bindings.orderCheckAvailable-in-0.group=processor-group",
                "spring.kafka.consumer.auto-offset-reset=earliest",
                "spring.cloud.stream.bindings.orderCheckAvailable-in-0.consumer.max-attempts=5",
                "spring.cloud.stream.bindings.orderCheckAvailable-in-0.consumer.back-off-initial-interval=1000",
                "spring.cloud.stream.bindings.orderCheckAvailable-in-0.consumer.back-off-multiplier=2.0",
                "spring.cloud.stream.bindings.orderCheckAvailable-in-0.consumer.back-off-max-interval=10000"

        }
)
public class TransactionalFailIntegrationTest extends ProcessorApplicationTests {

    @Autowired
    private KafkaTemplate<String,Object> kafkaTemplate;

    @Autowired
    private ConsumerFactory<String,Object> consumer;

    @MockitoBean
    private IRandomAvailableService service;



    @Test
    public void dbDown()
    {
        //given
        Mockito.when(service.isAvaiable()).thenThrow(new RuntimeException(""));
        OrderDto orderDto = new OrderDto("12A!","name",new BigDecimal("12.1"));
        kafkaTemplate.send("order-topic",orderDto);
        Consumer<String,Object> kafkaConsumer = consumer.createConsumer("processor-group-2","");
        kafkaConsumer.subscribe(List.of("stock-topic"));
        //when
        //then
        Awaitility.await().atMost(10, TimeUnit.SECONDS)
                .untilAsserted(
                        ()->
                        {
                        }
                );
        Assertions.assertThatThrownBy(()->
                        KafkaTestUtils.getSingleRecord(kafkaConsumer,
                                "stock-topic"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("No records found for topic");
    }

}
