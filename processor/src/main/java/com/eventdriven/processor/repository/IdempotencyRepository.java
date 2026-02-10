package com.eventdriven.processor.repository;

import com.eventdriven.processor.entity.IdempotencyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IdempotencyRepository extends JpaRepository<IdempotencyEntity,Long> {

	boolean existsByidEvent(String idEvent);
}
