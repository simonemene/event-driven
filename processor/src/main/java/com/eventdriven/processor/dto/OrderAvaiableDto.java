package com.eventdriven.processor.dto;

import com.eventdriven.processor.enums.ProcessorEnum;

public record OrderAvaiableDto(OrderDto order, String avaiable){}

