package com.eventdriven.sink.service;

import com.eventdriven.sink.dto.OrderAvailableDto;

public interface IConvertOrderService {

    void convertAndSave(OrderAvailableDto order);
}
