package com.eventdriven.dlq.repository;

import com.eventdriven.dlq.entity.OrderDlqEntity;
import com.eventdriven.dlq.entity.OrderStockDlqEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderStockDlqRepository extends JpaRepository<OrderStockDlqEntity,Long> {

	List<OrderStockDlqEntity> findByNotificationFalse();

	OrderStockDlqEntity findByIdEvent(String idEvento);
}
