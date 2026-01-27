package com.eventdriven.sink.entity;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.Instant;

@EntityListeners(AuditingEntityListener.class)
@Table(name = "ORDER_AVAILABLE")
@Entity
public class OrderAvailableEntity {

    public OrderAvailableEntity(String name,BigDecimal cost, String statusStock)
    {
        this.name = name;
        this.cost = cost;
        this.statusStock = statusStock;
    }

    protected OrderAvailableEntity()
    {

    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private BigDecimal cost;

    @Column(name = "STATUS_STOCK")
    private String statusStock;

    @CreatedDate
    @Column(name = "INSERT_ORDER")
    private Instant insertOrder;

    @Column(name = "ID_EVENT")
    String idEvent;
}
