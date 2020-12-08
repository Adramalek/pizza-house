package com.practicum.pizzahouse.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

@ExtendWith(SpringExtension.class)
public class AddressServiceTest {

  @Test
  public void testEstimateDelivery_SupplyAddress_ReturnDateInTheFuture() {
    AddressService addressService = new AddressServiceImpl(1);

    LocalDateTime actualDate = addressService.estimateDelivery("г. Казань, ул. Кремлевская 18");

    Assertions.assertTrue(LocalDateTime.now().isBefore(actualDate));
  }
}
