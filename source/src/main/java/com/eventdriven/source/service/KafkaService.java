package com.eventdriven.source.service;

import com.eventdriven.source.entity.MessageEntity;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
@RequiredArgsConstructor
public class KafkaService implements IBrokerService{

	private final KafkaTemplate<String,String> kafkaTemplate;

	@CircuitBreaker(name = "kafkaProducer",fallbackMethod = "fallback")
	public void send(MessageEntity event)
			throws ExecutionException, InterruptedException, TimeoutException {
		kafkaTemplate
				.send(event.getTopic(),event.getEventId(),event.getPayload())
				.get(5, TimeUnit.SECONDS);

	}

	public void fallback(MessageEntity event,Throwable ex)
	{
		throw new RuntimeException("kafka send failed",ex);
	}
}
