package com.eventdriven.processor.service;

import com.eventdriven.processor.dto.OrderDto;

public interface ISaveOrderService {

	void saveOrder(OrderDto order);
}
