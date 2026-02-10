package com.eventdriven.source.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record OrderDto(
        @NotBlank(message = "Order name must not be blank") String name,
        @NotNull(message = "Cost must no be null")
        @Positive(message = "Order cost must be greater than zero")
        BigDecimal cost,
        String eventiId) {

}
