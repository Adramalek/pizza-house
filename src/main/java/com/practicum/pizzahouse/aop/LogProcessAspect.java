package com.practicum.pizzahouse.aop;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LogProcessAspect {

  @SneakyThrows
  @Around("@annotation(com.practicum.pizzahouse.aop.LogProcess)")
  public Object logProcess(ProceedingJoinPoint joinPoint) {
    log.info("Start executing method {}", joinPoint.getSignature());
    long start = System.currentTimeMillis();
    Object result = joinPoint.proceed();
    long end = System.currentTimeMillis();
    log.info("Execution took {} ms", end - start);
    return result;
  }

  @Before("execution(public * com.practicum.pizzahouse.api.controller.PizzaController.*(..))")
  public void start() {
    log.info("Begin -----------");
  }

  @After("@within(com.practicum.pizzahouse.aop.LogProcess)")
  public void end() {
    log.info("End -------------");
  }
}
