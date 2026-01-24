package com.eventdriven.sink.repository;

import com.eventdriven.sink.entity.OrderAvaiableEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.Repository;

public interface OrderAvaiableRepository extends JpaRepository<OrderAvaiableEntity,Long> {
}
