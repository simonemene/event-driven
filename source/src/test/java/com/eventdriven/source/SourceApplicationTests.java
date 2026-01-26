package com.eventdriven.source;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
,properties = "spring.scheduling.time=100")
public class SourceApplicationTests {



}
