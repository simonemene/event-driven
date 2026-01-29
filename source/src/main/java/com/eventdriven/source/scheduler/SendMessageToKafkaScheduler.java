package com.eventdriven.source.scheduler;

import com.eventdriven.source.entity.MessageEntity;
import com.eventdriven.source.enums.StatusEnum;
import com.eventdriven.source.savemessage.MessageRespository;
import com.eventdriven.source.service.IBrokerService;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class SendMessageToKafkaScheduler {

	private final IBrokerService service;

	private final MessageRespository respository;


	@Scheduled(fixedDelayString = "${spring.scheduling.time}",
			initialDelayString = "${spring.scheduled.delay}")
	public void publish()
	{
		List<MessageEntity> lastMessage =
				respository.findTop10ByStatusInOrderByCreateTimestampAsc(
						List.of(StatusEnum.NEW,
								StatusEnum.FAILED));

		for(MessageEntity message : lastMessage)
		{
			try {
				message.setStatus(StatusEnum.IN_PROGRESS);
				message = respository.save(message);
				service.send(message);
				message.setStatus(StatusEnum.SEND);
				message.setSendTimestamp(Instant.now());
			}catch(OptimisticLockException lock)
			{
				log.info("Message idevent {} already processed by another pod",message.getEventId());
				continue;
			}catch(Exception e)
			{
				message.setAttempts(message.getAttempts() + 1);
				message.setStatus(message.getAttempts()>3?
						StatusEnum.PARKING:
						StatusEnum.FAILED);
				log.error("Failed message {} ", message.getEventId(),e);
			}
			respository.save(message);
		}
	}
}
