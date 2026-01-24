package com.eventdriven.sink.dto;

import com.eventdriven.processor.dto.OrderDto;

public record OrderAvaiableDto(OrderDto order, String avaiable){}

