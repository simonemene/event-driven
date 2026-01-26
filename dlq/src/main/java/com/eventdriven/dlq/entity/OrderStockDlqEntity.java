package com.eventdriven.dlq.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;


@Entity
@Table(name = "ORDER_STOCK_SLQ")
public class OrderStockDlqEntity {

	protected OrderStockDlqEntity()
	{

	}

	public OrderStockDlqEntity(String name,BigDecimal cost,String available)
	{
		this.name = name;
		this.cost = cost;
		this.available = available;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	private BigDecimal cost;

	private String available;


}
