package com.practicum.pizzahouse.model;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "ingredient")
public class Ingredient {
  @Id
  @Setter(AccessLevel.PROTECTED)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private BigDecimal price;

  public Ingredient(String name, BigDecimal price) {
    this.name = name;
    this.price = price;
  }
}
