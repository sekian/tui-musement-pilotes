package com.tui.proof.model;

import lombok.Data;

import javax.persistence.Embeddable;
@Data
@Embeddable
public class Address {
  private String street;
  private String postcode;
  private String city;
  private String country;
}
