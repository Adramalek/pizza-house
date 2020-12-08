package com.practicum.pizzahouse.service;

import com.practicum.pizzahouse.api.dto.OrderDetails;
import com.practicum.pizzahouse.api.dto.OrderSummary;
import com.practicum.pizzahouse.model.Ingredient;
import com.practicum.pizzahouse.model.IngredientRepository;
import com.practicum.pizzahouse.model.PizzaRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@SpringBootTest
@ExtendWith(SpringExtension.class)
public class PizzaServiceImplIntegrationTest {
  @Autowired
  private PizzaRepository pizzaRepository;

  @SpyBean
  private PizzaService pizzaService;

  @Autowired
  private IngredientRepository ingredientRepository;

  @BeforeEach
  public void prepareDB() {
    List<Ingredient> ingredients = List.of(
        new Ingredient("Сыр Пармезан", BigDecimal.valueOf(10)),
        new Ingredient("Сыр Дор Блю", BigDecimal.valueOf(15)),
        new Ingredient("Ветчина", BigDecimal.valueOf(10))
    );

    ingredientRepository.saveAll(ingredients);
  }

  @AfterEach
  public void clearDB() {
    pizzaRepository.deleteAll();
//    ingredientRepository.deleteAll();
  }

  @Test
  public void testMakeOrder_MakeOrder_DeliveryDelayOneDay() {

    OrderDetails orderDetails = OrderDetails.builder()
        .address("г. Казань, ул. Кремлевская 18")
        .customerName("Алексей")
        .thickness(3)
        .ingredient("Сыр Пармезан")
        .ingredient("Сыр Дор Блю")
        .ingredient("Ветчина")
        .build();

    OrderSummary orderSummary = pizzaService.makeOrder(orderDetails);

    Assertions.assertEquals(LocalDateTime.now().toLocalDate(),
        orderSummary.getDeliveryTime().minusDays(3).toLocalDate());

    Mockito.when(pizzaService.makeOrder(orderDetails))
        .thenReturn(null);

    Assertions.assertNull(pizzaService.makeOrder(orderDetails));
  }

  @TestConfiguration
  public static class Configuration {
    @Bean
    @Primary
    public AddressService addressService() {
      return address -> LocalDateTime.now().plusDays(3);
    }
  }
}
