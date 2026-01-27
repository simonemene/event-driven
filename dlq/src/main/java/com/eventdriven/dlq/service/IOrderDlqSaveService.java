package com.eventdriven.dlq.service;

import com.eventdriven.dlq.dto.OrderDto;

public interface IOrderDlqSaveService {

	void saveMessage(OrderDto message);
}
