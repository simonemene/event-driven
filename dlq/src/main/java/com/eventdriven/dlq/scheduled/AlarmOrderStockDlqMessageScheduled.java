package com.eventdriven.dlq.scheduled;

import com.eventdriven.dlq.dto.OrderAvailableDto;
import com.eventdriven.dlq.dto.OrderDto;
import com.eventdriven.dlq.service.IOrderSupportQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Component
public class AlarmOrderStockDlqMessageScheduled implements IAlarmScheduled{

	private final IOrderSupportQueryService<OrderAvailableDto> service;

	@Scheduled(fixedDelayString = "${spring.scheduled.time}")
	@Override
	public void alarm() {
		List<OrderAvailableDto> listAlarmOrder = service.getElmentDlq();
		for(OrderAvailableDto order : listAlarmOrder)
		{
			log.info("[ORDER IN STOCK] ALARM: {}", order.order().nameOrder());
			service.notification(order);
		}
	}
}
