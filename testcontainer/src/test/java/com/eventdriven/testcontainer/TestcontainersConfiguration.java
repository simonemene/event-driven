package com.eventdriven.testcontainer;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.images.builder.ImageFromDockerfile;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.utility.DockerImageName;


@TestConfiguration(proxyBeanMethods = false)
class TestcontainersConfiguration {

	@Bean
	@ServiceConnection
	KafkaContainer kafkaContainer() {
		return new KafkaContainer(
				DockerImageName.parse("apache/kafka:3.7.0")
		);
	}


	@Bean
	NewTopic orderTopic() {
		return TopicBuilder.name("order-topic")
				.partitions(1)
				.replicas(1)
				.build();
	}

	@Bean
	NewTopic stockTopic() {
		return TopicBuilder.name("stock-topic")
				.partitions(1)
				.replicas(1)
				.build();
	}

	@Bean
	NewTopic dlqOrderTopic() {
		return TopicBuilder.name("order-topic.dlq")
				.partitions(1)
				.replicas(1)
				.build();
	}

	@Bean
	NewTopic dlqStockTopic() {
		return TopicBuilder.name("stock-topic.dlq")
				.partitions(1)
				.replicas(1)
				.build();
	}

	@Bean
	NewTopic configServerTopic() {
		return TopicBuilder.name("spring-cloud-bus")
				.partitions(1)
				.replicas(1)
				.build();
	}


	@Bean
	@ServiceConnection
	MySQLContainer<?> mySqlContainer() {
		return new MySQLContainer<>("mysql:8.0")
				.withDatabaseName("test")
				.withUsername("user")
				.withPassword("sa");
	}

	static GenericContainer<?> configServerContainer =
			new GenericContainer<>(
					new ImageFromDockerfile()
							.withFileFromClasspath(
									"app.jar",
									"configserver-0.0.1-SNAPSHOT.jar"
							)
							.withDockerfileFromBuilder(builder ->
									builder
											.from("eclipse-temurin:21-jre")
											.copy("app.jar", "/app.jar")
											.entryPoint("java", "-jar", "/app.jar")
											.build()
							)
			)
					.withExposedPorts(8888)
					.waitingFor(Wait.forHttp("/actuator/health"));





}
