package com.eventdriven.dlq.repository;

import com.eventdriven.dlq.entity.OrderDlqEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDlqRepository extends JpaRepository<OrderDlqEntity,Long> {
}
