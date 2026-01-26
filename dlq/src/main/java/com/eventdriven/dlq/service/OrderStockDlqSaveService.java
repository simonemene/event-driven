package com.eventdriven.dlq.service;

import com.eventdriven.dlq.repository.OrderStockDlqRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OrderStockDlqSaveService {

	private final OrderStockDlqRepository repository;
}
