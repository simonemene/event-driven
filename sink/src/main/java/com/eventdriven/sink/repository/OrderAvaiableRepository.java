package com.eventdriven.sink.repository;

import com.eventdriven.sink.entity.OrderAvailableEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderAvaiableRepository extends JpaRepository<OrderAvailableEntity,Long> {
}
