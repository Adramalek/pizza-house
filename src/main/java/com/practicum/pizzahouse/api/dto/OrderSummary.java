package com.practicum.pizzahouse.api.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@EqualsAndHashCode
public class OrderSummary {
  private final BigDecimal totalPrice;
  private final LocalDateTime deliveryTime;
  private final String customerName;

  @JsonCreator
  public OrderSummary(@JsonProperty("totalPrice") BigDecimal totalPrice,
                      @JsonProperty("deliveryTime") LocalDateTime deliveryTime,
                      @JsonProperty("customerName") String customerName) {
    this.totalPrice = totalPrice;
    this.deliveryTime = deliveryTime;
    this.customerName = customerName;
  }
}
