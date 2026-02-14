package com.eventdriven.processor.kafkafail;

import com.eventdriven.processor.ProcessorApplicationTests;
import com.eventdriven.processor.dto.OrderDto;
import com.eventdriven.processor.repository.IdempotencyRepository;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.assertj.core.api.Assertions;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.math.BigDecimal;
import java.time.Duration;
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
public class DownDBIntegrationTest extends ProcessorApplicationTests {

    @Autowired
    private KafkaTemplate<String,Object> kafkaTemplate;

    @Autowired
    private ConsumerFactory<String,Object> consumer;

    @MockitoBean
    private IdempotencyRepository repository;


    @Test
    public void dbDown()
    {
        //given
        Mockito.when(repository.save(Mockito.any())).thenThrow(new BadSqlGrammarException("db down","", null));
        OrderDto orderDto = new OrderDto("12A!","name",new BigDecimal("12.1"),"1233");
        kafkaTemplate.send("order-topic",orderDto);
        Consumer<String,Object> kafkaConsumer = consumer.createConsumer("processor-group","");
        kafkaConsumer.subscribe(List.of("order-topic.dlq"));
        //when
        //then
        Awaitility.await().atMost(25, TimeUnit.SECONDS)
                .untilAsserted(
                        ()->
                        {
                            ConsumerRecord<String,Object> record =
                                    KafkaTestUtils.getSingleRecord(kafkaConsumer,
                                            "order-topic.dlq");
                            Mockito.verify(repository,Mockito.times(5)).save(Mockito.any());
                            Assertions.assertThat(record).isNotNull();
                        }
                );

    }

}
