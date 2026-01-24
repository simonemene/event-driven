package com.eventdriven.sink.service;

import com.eventdriven.sink.dto.OrderAvaiableDto;

public interface IConvertOrderService {

    void convertAndSave(OrderAvaiableDto order);
}
