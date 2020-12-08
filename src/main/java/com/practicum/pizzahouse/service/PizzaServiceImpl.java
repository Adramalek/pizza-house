package com.practicum.pizzahouse.service;

import com.practicum.pizzahouse.api.dto.OrderDetails;
import com.practicum.pizzahouse.api.dto.OrderSummary;
import com.practicum.pizzahouse.exception.PizzaServiceException;
import com.practicum.pizzahouse.model.Ingredient;
import com.practicum.pizzahouse.model.IngredientRepository;
import com.practicum.pizzahouse.model.Pizza;
import com.practicum.pizzahouse.model.PizzaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Slf4j
@Service
@Validated
@RequiredArgsConstructor
public class PizzaServiceImpl implements PizzaService {
  private final AddressService addressService;
  private final IngredientRepository ingredientRepository;
  private final PizzaRepository pizzaRepository;

  @Override
  @Transactional
  public OrderSummary makeOrder(@NotNull OrderDetails orderDetails) {
    List<Ingredient> ingredients = ingredientRepository.findAllByNameIn(orderDetails.getIngredients());
    checkIngredients(orderDetails.getIngredients(), ingredients);
    Pizza pizza = new Pizza(orderDetails.getThickness(), ingredients);
    pizzaRepository.save(pizza);
    log.info("Saved pizza {}", pizza);

    BigDecimal totalPrice = ingredients.stream()
        .map(Ingredient::getPrice)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
    LocalDateTime deliveryTime = addressService.estimateDelivery(orderDetails.getAddress());
    return new OrderSummary(totalPrice, deliveryTime, orderDetails.getCustomerName());
  }

  private void checkIngredients(Collection<String> ingredientNames, Collection<Ingredient> ingredients) {
    log.info("Check ingredients");
    Set<String> existingIngredients = ingredients.stream()
        .map(Ingredient::getName)
        .collect(Collectors.toSet());
    List<String> missedIngredients = ingredientNames.stream()
        .filter(Predicate.not(existingIngredients::contains))
        .collect(Collectors.toList());
    if (!missedIngredients.isEmpty()) {
      throw new PizzaServiceException(
          "Некоторые ингредиенты отсутсвуют, пожалуйста, переоформите заказ",
          missedIngredients
      );
    }
  }
}
