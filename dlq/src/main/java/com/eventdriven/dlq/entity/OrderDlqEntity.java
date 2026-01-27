package com.eventdriven.dlq.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@Table(name = "ORDER_DLQ")
@Entity
public class OrderDlqEntity {

	protected OrderDlqEntity()
	{

	}

	public OrderDlqEntity(String idEvent,String name,BigDecimal cost,boolean notification)
	{
		this.idEvent = idEvent;
		this.name = name;
		this.cost = cost;
		this.notification = notification;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	private BigDecimal cost;

	private boolean notification;

	@Column(name = "ID_EVENT")
	String idEvent;
}
