package com.eventdriven.dlq.repository;

import com.eventdriven.dlq.entity.OrderStockDlqEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderStockDlqRepository extends JpaRepository<OrderStockDlqEntity,Long> {
}
