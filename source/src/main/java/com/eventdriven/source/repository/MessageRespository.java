package com.eventdriven.source.repository;

import com.eventdriven.source.entity.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRespository extends JpaRepository<MessageEntity,Long> {

	List<MessageEntity> findTop10ByStatusByCreatedTimestampAsc(List<String> status);
}
