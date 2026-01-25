package com.eventdriven.sink.mapper;

import com.eventdriven.sink.dto.OrderAvailableDto;
import com.eventdriven.sink.entity.OrderAvailableEntity;

public class OrderMapper {

    public OrderAvailableEntity toEntity(OrderAvailableDto dto)
    {
        return OrderAvailableEntity
                .builder()
                .cost(dto.order().costOrder())
                .name(dto.order().nameOrder())
                .statusStock(dto.avaiable())
                .build();
    }
}
