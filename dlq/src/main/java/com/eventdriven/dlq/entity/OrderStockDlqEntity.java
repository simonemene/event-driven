package com.eventdriven.dlq.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Entity
@Table(name = "ORDER_STOCK_DLQ")
public class OrderStockDlqEntity {

	protected OrderStockDlqEntity()
	{

	}

	public OrderStockDlqEntity(String name,BigDecimal cost,String available,boolean notification)
	{
		this.name = name;
		this.cost = cost;
		this.available = available;
		this.notification = notification;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	private BigDecimal cost;

	private String available;

	private boolean notification;




}
