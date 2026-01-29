package com.eventdriven.source.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Profile("!e2e")
@EnableJpaAuditing
@Configuration
public class JpaEnabledConfiguration {
}
