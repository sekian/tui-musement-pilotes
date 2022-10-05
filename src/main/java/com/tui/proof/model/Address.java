package com.tui.proof.model;

import lombok.Data;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Embeddable
public class Address {
  @NotNull(message = "street cannot be null")
  @Size(max = 95, message = "street must be between 4 and 12 characters")
  private String street;
  @NotNull(message = "postcode cannot be null")
  @Size(max = 18, message = "postcode must be not be longer than 18 characters")
  private String postcode;
  @NotNull(message = "city cannot be null")
  @Size(max = 35, message = "city must be not be longer than 12 characters")
  private String city;
  @NotNull(message = "country cannot be null")
  @Size(max = 56, message = "country must be not be longer than 12 characters")
  private String country;
}
