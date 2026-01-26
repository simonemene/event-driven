package com.eventdriven.sink.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CurrentTimestamp;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Table(name = "ORDER_AVAILABLE")
@Entity
public class OrderAvailableEntity {

    public OrderAvailableEntity(String name,BigDecimal cost, String statusStock)
    {
        this.name = name;
        this.cost = cost;
        this.statusStock = statusStock;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private BigDecimal cost;

    @Column(name = "STATUS_STOCK")
    private String statusStock;

    @CurrentTimestamp
    @Column(name = "INSERT_ORDER")
    private Timestamp insertOrder;
}
