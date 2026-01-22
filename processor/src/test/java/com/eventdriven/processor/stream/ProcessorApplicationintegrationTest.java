package com.eventdriven.processor.stream;

import com.eventdriven.processor.ProcessorApplicationTests;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;

@Import(TestChannelBinderConfiguration.class)
public class ProcessorApplicationintegrationTest extends ProcessorApplicationTests {
}
