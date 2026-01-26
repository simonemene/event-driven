package com.eventdriven.dlq.service;

import com.eventdriven.dlq.repository.OrderDlqRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OrderDlqSaveService {

	private final OrderDlqRepository repository;
}
