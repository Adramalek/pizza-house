package com.practicum.pizzahouse.api.controller;

import com.practicum.pizzahouse.api.dto.OrderDetails;
import com.practicum.pizzahouse.api.dto.OrderSummary;
import com.practicum.pizzahouse.aop.LogProcess;
import com.practicum.pizzahouse.service.PizzaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
public class PizzaController {
  private final PizzaService pizzaService;

  @LogProcess
  @PostMapping(value = "/pizza/order",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  public ResponseEntity<String> orderPizza(@NotNull @Valid @RequestBody OrderDetails orderDetails) {
//    OrderSummary result = pizzaService.makeOrder(orderDetails);
    foo();
    return ResponseEntity.ok("Pizza");
  }

  private void foo() {
    log.info("FOO");
  }
}
