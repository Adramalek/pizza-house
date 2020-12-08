package com.practicum.pizzahouse.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.practicum.pizzahouse.api.dto.OrderDetails;
import com.practicum.pizzahouse.api.dto.OrderSummary;
import com.practicum.pizzahouse.service.PizzaService;
import lombok.SneakyThrows;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@WebMvcTest
@ExtendWith(SpringExtension.class)
public class PizzaControllerTest {

  @Autowired
  private MockMvc mvc;

  @MockBean
  private PizzaService pizzaService;

  @Test
  @SneakyThrows
  public void testOrderPizza_SupplyCorrectOrder_ReturnsCorrectSummary() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.disable(SerializationFeature.WRAP_ROOT_VALUE);

    LocalDateTime date = LocalDateTime.now().plusHours(1L);
    OrderDetails orderDetails = OrderDetails.builder()
        .address("г. Казань, ул. Кремлевская 18")
        .customerName("Алексей")
        .thickness(3)
        .ingredient("Сыр Пармезан")
        .ingredient("Сыр Дор Блю")
        .ingredient("Ветчина")
        .build();
    OrderSummary expectedOrderSummary = new OrderSummary(BigDecimal.valueOf(35), date, "Алексей");

    Mockito.when(pizzaService.makeOrder(orderDetails))
        .thenReturn(expectedOrderSummary);

    mvc.perform(MockMvcRequestBuilders.post("/pizza/order")
          .content(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(orderDetails))
          .contentType(MediaType.APPLICATION_JSON))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.jsonPath("$.totalPrice", Matchers.is(BigDecimal.valueOf(35)), BigDecimal.class))
        .andExpect(MockMvcResultMatchers.jsonPath("$.deliveryTime", Matchers.greaterThan(LocalDateTime.now())))
        .andExpect(MockMvcResultMatchers.jsonPath("$.customerName", Matchers.is("Алексей")));
  }
}
