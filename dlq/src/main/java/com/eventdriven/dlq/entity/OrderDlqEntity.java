package com.eventdriven.dlq.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Table(name = "ORDER_DLQ")
@Entity
public class OrderDlqEntity {

	protected OrderDlqEntity()
	{

	}

	public OrderDlqEntity(String name,BigDecimal cost)
	{
		this.name = name;
		this.cost = cost;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	private BigDecimal cost;
}
