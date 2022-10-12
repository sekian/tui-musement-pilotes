package com.tui.proof.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.tui.proof.validator.OneOf;
import com.tui.proof.view.Views;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.Objects;

@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "orders")
@ToString
@Entity
public class Order {
  @Id
  @GeneratedValue(generator="hibernate-uuid")
  @GenericGenerator(name="hibernate-uuid", strategy = "uuid2")
  @Schema(hidden = true)
  @JsonView(Views.Public.class)
  private String number;

  @JsonView(Views.Public.class)
  private Address deliveryAddress;

  @JsonView(Views.Public.class)
  @Min(value = 5, message = "pilotes should not be less than 5")
  @Max(value = 15, message = "pilotes should not be greater than 15")
  @OneOf(value = {5, 10, 15}, message = "pilotes must be 5, 10 or 15")
  private int pilotes;

  @JsonView(Views.Public.class)
  @Schema(hidden = true)
  private double orderTotal;

  @JsonView(Views.Public.class)
//  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Instant timestamp;

  @JsonIgnore
  private static double PILOTES_PRICE = 1.33;
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name="clientId", nullable=false)
//  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  @ToString.Exclude
  @Schema(hidden = true)
  @JsonView(Views.Public.class)
  private Client client;

  public void updateOrderTotal() {
    this.orderTotal = computeOrderTotal(pilotes);
  }

  private double round(double number) {
    return new BigDecimal(number).setScale(2, RoundingMode.HALF_UP).doubleValue();
  }

  private double computeOrderTotal(int pilotes) {
    double orderTotal =  pilotes * PILOTES_PRICE;
    return round(orderTotal);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    Order order = (Order) o;
    return number != null && Objects.equals(number, order.number);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }

}

