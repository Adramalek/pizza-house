package com.practicum.pizzahouse.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {

  List<Ingredient> findAllByNameIn(Collection<String> ingredientNames);

}
