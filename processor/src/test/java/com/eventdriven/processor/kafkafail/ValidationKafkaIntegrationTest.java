package com.eventdriven.processor.kafkafail;

import com.eventdriven.processor.ProcessorApplicationTests;
import com.eventdriven.processor.dto.OrderDto;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.assertj.core.api.Assertions;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.ExecutionException;
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
                "spring.kafka.consumer.auto-offset-reset=earliest"
        }
)
public class ValidationKafkaIntegrationTest extends ProcessorApplicationTests {

    @Autowired
    private KafkaTemplate<String,Object> kafkaTemplate;

    @Autowired
    private ConsumerFactory<String,Object> consumer;

    @Test
    public void validationDlq() throws ExecutionException, InterruptedException {
        //given
        OrderDto orderDto = new OrderDto("","example",new BigDecimal("12.1"),"122344");
        kafkaTemplate.send("order-topic",orderDto).get();
        Consumer<String, Object> kafkaConsumer  = consumer.createConsumer("processor-group","");
        kafkaConsumer.subscribe(List.of("order-topic.dlq"));
        //when
        //then
        Awaitility.await()
                .atMost(10, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    ConsumerRecord<String, Object> record =
                            KafkaTestUtils.getSingleRecord(
                                    kafkaConsumer,
                                    "order-topic.dlq"
                            );

                    Assertions.assertThat(record).isNotNull();
                });
    }
}
