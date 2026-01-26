package com.eventdriven.source.enums;

import lombok.Getter;

@Getter
public enum StatusEnum {

  NEW("NEW"),
  PARKING("PARKING"),
  FAILED("FAILED"),
  IN_PROGRESS("IN_PROGRESS"),
  SEND("SEND");

  private String value;

  StatusEnum(String value)
  {
	  this.value = value;
  }
}
