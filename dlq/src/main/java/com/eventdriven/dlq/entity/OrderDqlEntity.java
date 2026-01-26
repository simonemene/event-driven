package com.eventdriven.dlq.entity;

import jakarta.persistence.*;

@Table(name = "ORDER_DLQ")
@Entity
public class OrderDqlEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
}
