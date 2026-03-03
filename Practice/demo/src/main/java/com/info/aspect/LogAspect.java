package com.info.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogAspect {
	@Around("execution(* com.info.service.Service.hello(..))")
	public Object logApplicationStart(ProceedingJoinPoint joinPoint) throws Throwable{
		System.out.println("AOP APPlication Started Successfully : Message from LogAspect");
		Object obj = joinPoint.proceed();
		System.out.println("AOP Around: After Hello");
		return obj;
		}

}
