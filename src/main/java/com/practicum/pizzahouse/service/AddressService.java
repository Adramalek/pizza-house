package com.practicum.pizzahouse.service;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public interface AddressService {
  LocalDateTime estimateDelivery(@NotBlank String address);
}
