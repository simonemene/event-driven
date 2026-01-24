package com.eventdriven.sink.mapper;

import com.eventdriven.sink.dto.OrderAvaiableDto;
import com.eventdriven.sink.entity.OrderAvaiableEntity;

public class OrderMapper {

    public OrderAvaiableEntity toEntity(OrderAvaiableDto dto)
    {
        return OrderAvaiableEntity
                .builder()
                .cost(dto.order().costOrder())
                .name(dto.order().nameOrder())
                .statusStock(dto.avaiable())
                .build();
    }
}
