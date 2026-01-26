package com.eventdriven.dlq.entity;

import jakarta.persistence.*;

@Table(name = "ORDER_DLQ")
@Entity
public class OrderDlqEntity {

	protected OrderDlqEntity()
	{

	}

	public OrderDlqEntity(String name,String cost)
	{
		this.name = name;
		this.cost = cost;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	private String cost;
}
