package com.eventdriven.dlq.service;

import java.util.List;

public interface IOrderSupportQueryService<T> {

	List<T> getElmentDlq();

	void saveMessage(T element);
}
