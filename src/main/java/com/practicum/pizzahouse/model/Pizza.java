package com.practicum.pizzahouse.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "pizza")
public class Pizza {
  @Id
  @Setter(AccessLevel.PROTECTED)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "thickness")
  private long thickness;

  @Singular
  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "pizza_ingredient", joinColumns = {
      @JoinColumn(name = "ingredient_id", referencedColumnName = "id"),
      @JoinColumn(name = "pizza_id", referencedColumnName = "id")
  })
  private List<Ingredient> ingredients;

  public Pizza(long thickness, List<Ingredient> ingredients) {
    this.thickness = thickness;
    this.ingredients = ingredients;
  }

  @Override
  public String toString() {
    String ingredientsListString = ingredients.stream()
        .map(Ingredient::getName)
        .collect(Collectors.joining(", "));
    return "Pizza{" +
        "thickness=" + thickness +
        ", ingredients=[" + ingredientsListString +
        "]}";
  }
}
