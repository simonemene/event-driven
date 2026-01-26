package com.eventdriven.source.scheduler;

import com.eventdriven.source.entity.MessageEntity;
import com.eventdriven.source.enums.StatusEnum;
import com.eventdriven.source.repository.MessageRespository;
import com.eventdriven.source.service.IBrokerService;
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


	@Scheduled(fixedDelayString = "30000")
	public void pusblish()
	{
		List<MessageEntity> lastMessage =
				respository.findTop10ByStatusInOrderByCreateTimestampAsc(
						List.of(StatusEnum.NEW,
								StatusEnum.FAILED));

		for(MessageEntity message : lastMessage)
		{
			try{
				service.send(message);
				message.setStatus(StatusEnum.SEND);
				message.setSendTimestamp(Instant.now());
			}catch(Exception e)
			{
				message.setAttempts(message.getAttempts() + 1);
				if(message.getAttempts()>3)
				{
					message.setStatus(StatusEnum.PARKING);
				}else
				{
					message.setStatus(StatusEnum.FAILED);
				}
				log.error("Failed message {} ", message.getEventId(),e);
			}
			respository.save(message);
		}
	}
}
