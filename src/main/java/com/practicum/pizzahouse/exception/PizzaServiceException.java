package com.practicum.pizzahouse.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@Getter
@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class PizzaServiceException extends RuntimeException {
  private final List<String> missedIngredients;

  public PizzaServiceException(String message, List<String> missedIngredients) {
    super(message);
    this.missedIngredients = missedIngredients;
  }
}
