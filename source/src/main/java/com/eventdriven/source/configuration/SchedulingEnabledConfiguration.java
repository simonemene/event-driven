package com.eventdriven.source.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;

@Profile("!e2e")
@EnableScheduling
@Configuration
public class SchedulingEnabledConfiguration {
}
