package com.eventdriven.dlq.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@Entity
@Table(name = "ORDER_STOCK_DLQ")
public class OrderStockDlqEntity {

	protected OrderStockDlqEntity()
	{

	}

	public OrderStockDlqEntity(String idEvent,String name,BigDecimal cost,String available,boolean notification)
	{
		this.name = name;
		this.cost = cost;
		this.available = available;
		this.notification = notification;
		this.idEvent = idEvent;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	private BigDecimal cost;

	private String available;

	private boolean notification;

	@Column(name = "ID_EVENT")
	String idEvent;




}
