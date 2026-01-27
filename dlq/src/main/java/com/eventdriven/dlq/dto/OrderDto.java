package com.eventdriven.dlq.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

import java.math.BigDecimal;

public record OrderDto(
        @NotBlank(message = "Id must not be blank") String id,
        @NotBlank(message = "Order name must not be blank") String nameOrder,
        @NotNull(message = "Cost must no be null")
        @Positive(message = "Order cost must be greater than zero")
        BigDecimal costOrder,
        boolean notification) {

        public OrderDto(String id,String nameOrder,BigDecimal costOrder)
        {
                this(id,nameOrder,costOrder,false);
        }

}
