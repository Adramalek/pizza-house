package com.practicum.pizzahouse.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Service
@Validated
public class AddressServiceImpl implements AddressService {
  private final int amount;

  public AddressServiceImpl(@Value("${pizza.delivery.amount}") int amount) {
    this.amount = amount;
  }

  @Override
  public LocalDateTime estimateDelivery(@NotBlank String address) {
    return LocalDateTime.now().plusDays(amount);
  }
}
