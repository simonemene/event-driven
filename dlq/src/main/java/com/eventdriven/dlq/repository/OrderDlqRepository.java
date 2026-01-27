package com.eventdriven.dlq.repository;

import com.eventdriven.dlq.entity.OrderDlqEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderDlqRepository extends JpaRepository<OrderDlqEntity,Long> {

	List<OrderDlqEntity> findByNotificationFalse();

	OrderDlqEntity findByIdEvent(String idEvento);
}
