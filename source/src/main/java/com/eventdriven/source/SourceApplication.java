package com.eventdriven.source;

import com.eventdriven.source.properties.CloudConfig;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@OpenAPIDefinition
		(
				info=
						@Info(
								title="Source Application",
								version="1.0.0",
								description = "Application to check if an order is available in stock",
								contact =
										@Contact(
												name="Simone Meneghetti",
												email = "s.meneghetti7@gmail.com"
										)
						),
				externalDocs =
						@ExternalDocumentation(
								description="Source Application",
								url="https://github.com/simone-meneghetti/event-driven"
				)

		)
@EnableConfigurationProperties(CloudConfig.class)
@EnableJpaAuditing
@EnableScheduling
@SpringBootApplication
public class SourceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SourceApplication.class, args);
	}

}
