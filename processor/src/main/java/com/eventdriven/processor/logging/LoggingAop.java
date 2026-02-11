package com.eventdriven.processor.logging;

import com.eventdriven.processor.exception.IdEventDuplicateException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LoggingAop {

	@AfterThrowing(value = "execution(com.eventdriven.processor.service.*.saveOrder(..)", throwing = "e")
	public void logging(JoinPoint joinPoint,Throwable e)
	{
		if(e instanceof IdEventDuplicateException)
		{
			log.warn("""
					[MESSAGE] = {}
					[IDEVENT] = {}
					""",e.getCause(),joinPoint.getArgs());
		}
	}
}
