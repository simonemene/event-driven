package com.eventdriven.dlq.scheduled;

import com.eventdriven.dlq.dto.OrderDto;
import com.eventdriven.dlq.service.IOrderSupportQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class AlarmOrderDlqMessageScheduled implements IAlarmScheduled{

	private final IOrderSupportQueryService<OrderDto> service;

	@Scheduled(fixedDelayString = "${spring.scheduled.time}",
	initialDelayString = "${spring.scheduled.delay}")
	@Override
	public void alarm() {
		List<OrderDto> listAlarmOrder = service.getElmentDlq();
		for(OrderDto order : listAlarmOrder)
		{
			log.info("[ORDER] ALARM: {}", order.id());
			service.notification(order);
		}
	}
}
