package com.eventdriven.processor.exception;

import jakarta.validation.ConstraintViolation;

import java.util.Set;

public class InvalidEventException extends RuntimeException {

  private final Set<? extends ConstraintViolation<?>> violations;

  public InvalidEventException(Set<? extends ConstraintViolation<?>> violations) {
    super("Invalid event payload");
    this.violations = violations;
  }

  public Set<? extends ConstraintViolation<?>> getViolations() {
    return violations;
  }
}

