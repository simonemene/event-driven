package com.eventdriven.sink.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record OrderAvailableDto(
        @Valid @NotNull OrderDto order,
        @NotEmpty String available){}

