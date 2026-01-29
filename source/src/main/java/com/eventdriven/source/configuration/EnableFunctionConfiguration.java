package com.eventdriven.source.configuration;

import com.eventdriven.source.properties.CloudConfig;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(CloudConfig.class)
public class EnableFunctionConfiguration {
}
