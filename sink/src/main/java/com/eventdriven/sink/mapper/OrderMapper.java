package com.eventdriven.sink.mapper;

import com.eventdriven.sink.dto.OrderAvailableDto;
import com.eventdriven.sink.entity.OrderAvailableEntity;

public class OrderMapper {

    public OrderAvailableEntity toEntity(OrderAvailableDto dto)
    {
        return new OrderAvailableEntity(dto.order().name(),
                dto.order().cost(),
                dto.available());
    }
}
