package com.eventdriven.dlq.service;

import com.eventdriven.dlq.dto.OrderAvailableDto;

public interface IOrderStockDlqSaveService {

	void saveMessage(OrderAvailableDto dto);
}
