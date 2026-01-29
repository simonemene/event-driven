package com.eventdriven.testcontainer;


import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles({"e2e"})
@Import(TestcontainersConfiguration.class)
public class TestcontainerApplicationTests {




}
