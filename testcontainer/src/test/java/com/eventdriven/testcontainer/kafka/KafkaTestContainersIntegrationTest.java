package com.eventdriven.testcontainer.kafka;

import com.eventdriven.processor.ProcessorApplication;
import com.eventdriven.sink.SinkApplication;
import com.eventdriven.source.SourceApplication;
import com.eventdriven.testcontainer.TestcontainerApplicationTests;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Import;

public class KafkaTestContainersIntegrationTest extends TestcontainerApplicationTests {


    @Test
    public void kafka()
    {

    }


    @EnableAutoConfiguration
    @Import({
            SourceApplication.class, SinkApplication.class, ProcessorApplication.class
    })
    public static class application
    {

    }
}
