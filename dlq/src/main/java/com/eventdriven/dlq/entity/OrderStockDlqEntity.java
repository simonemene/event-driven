package com.eventdriven.dlq.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "ORDER_STOCK_SLQ")
public class OrderStockDlqEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
}
