package com.practicum.pizzahouse.service;

import com.practicum.pizzahouse.api.dto.OrderDetails;
import com.practicum.pizzahouse.api.dto.OrderSummary;

import javax.validation.constraints.NotNull;

public interface PizzaService {
  OrderSummary makeOrder(@NotNull OrderDetails orderDetails);
}
