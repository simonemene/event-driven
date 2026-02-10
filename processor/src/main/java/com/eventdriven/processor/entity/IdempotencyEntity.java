package com.eventdriven.processor.entity;

import jakarta.persistence.*;

@Table(name = "IDEMPOTENCY")
@Entity
public class IdempotencyEntity {

	protected IdempotencyEntity()
	{

	}

	public IdempotencyEntity(String idEvent)
	{
		this.idEvent = idEvent;
	}

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private long id;

	@Column(name = "ID_EVENT",nullable = false,unique = true)
	private String idEvent;
}
