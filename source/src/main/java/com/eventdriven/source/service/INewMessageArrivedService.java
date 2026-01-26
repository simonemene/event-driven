package com.eventdriven.source.service;

import com.eventdriven.source.dto.OrderDto;

public interface INewMessageArrivedService {

	void newMessageArrived(OrderDto order);
}
