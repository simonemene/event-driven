package com.eventdriven.sink.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Builder
@Getter
@Table(name = "ORDER_AVAILABLE")
@Entity
public class OrderAvailableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private BigDecimal cost;

    @Column(name = "STATUS_STOCK")
    private String statusStock;

    @Column(name = "INSERT_ORDER")
    private Timestamp insertOrder;
}
