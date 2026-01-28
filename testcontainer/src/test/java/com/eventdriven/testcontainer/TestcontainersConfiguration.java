package com.eventdriven.testcontainer;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.HashMap;
import java.util.Map;

@TestConfiguration(proxyBeanMethods = false)
class TestcontainersConfiguration {

	@Bean
	@ServiceConnection
	KafkaContainer kafkaContainer() {
		return new KafkaContainer(
				DockerImageName.parse("apache/kafka-native:latest"));
	}

	@Bean
	KafkaAdmin kafkaAdmin(KafkaProperties kafkaProperties)
	{
		Map<String,Object> configs = new HashMap<>();
		configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG,
				kafkaProperties.getBootstrapServers());
		return new KafkaAdmin(configs);
	}

	@Bean

	NewTopic orderTopic()
	{
		return TopicBuilder.name("order-topic")
				.partitions(1)
				.replicas(1)
				.build();
	}



	@Bean
	@ServiceConnection
	MySQLContainer<?> mySqlContainer()
	{
		return new MySQLContainer<>("mysql:8.0")
				.withDatabaseName("appdb")
				.withUsername("user")
				.withPassword("sa");
	}

}
