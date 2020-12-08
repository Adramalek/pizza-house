package com.practicum.pizzahouse.api.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Singular;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import java.util.List;

@Getter
@EqualsAndHashCode
public class OrderDetails {
  private final String customerName;
  private final String address;
  private final List<String> ingredients;
  private final long thickness;

  @Builder
  @JsonCreator
  public OrderDetails(@NotBlank @JsonProperty("customerName") String customerName,
                      @NotBlank @JsonProperty("address") String address,
                      @Singular @NotEmpty @JsonProperty("ingredients") List<@NotBlank String> ingredients,
                      @Positive @JsonProperty("thickness") long thickness) {
    this.customerName = customerName;
    this.address = address;
    this.ingredients = ingredients;
    this.thickness = thickness;
  }
}
