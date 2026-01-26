package com.eventdriven.dlq.repository;

import com.eventdriven.dlq.entity.OrderDqlEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDlqRepository extends JpaRepository<OrderDqlEntity,Long> {
}
