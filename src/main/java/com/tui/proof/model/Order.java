package com.tui.proof.model;

import lombok.Data;
import javax.persistence.*;

@Data
@Table(name = "orders")
@Entity
public class Order {
  @Id
  @GeneratedValue(generator="system-uuid")
  @GenericGenerator(name="system-uuid", strategy = "uuid")
  private String number;
  private Address deliveryAddress;
  private int pilotes;
  private double orderTotal;

}
