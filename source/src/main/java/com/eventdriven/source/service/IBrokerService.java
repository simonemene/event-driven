package com.eventdriven.source.service;

import com.eventdriven.source.entity.MessageEntity;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public interface IBrokerService {

	void send(MessageEntity event) throws ExecutionException, InterruptedException,
			TimeoutException;

}
