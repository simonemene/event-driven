package com.eventdriven.dlq.dto;

public record OrderAvailableDto(OrderDto order, String avaiable,boolean notification){

	public OrderAvailableDto(OrderDto order,String available)
	{
		this(order,available,false);
	}

}

