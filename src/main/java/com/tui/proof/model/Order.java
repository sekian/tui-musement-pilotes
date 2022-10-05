package com.tui.proof.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tui.proof.validator.OneOf;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.Date;

@Data
@Table(name = "orders")
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class Order {
  @Id
  @GeneratedValue(generator="system-uuid")
  @GenericGenerator(name="system-uuid", strategy = "uuid")
  private String number;
  private Address deliveryAddress;
  @Min(value = 5, message = "pilotes should not be less than 5")
  @Max(value = 15, message = "pilotes should not be greater than 15")
  @OneOf(value = {5, 10, 15}, message = "pilotes must be 5, 10 or 15")
  private int pilotes;
  private double orderTotal;
  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Instant timestamp;
  private double PILOTES_PRICE = 1.33;

  public void recomputeOrderTotal() {
    this.orderTotal = computeOrderTotal(pilotes);
  }

  private double round(double number) {
    return new BigDecimal(number).setScale(2, RoundingMode.HALF_UP).doubleValue();
  }

  private double computeOrderTotal(int pilotes) {
    double orderTotal =  pilotes * PILOTES_PRICE;
    return round(orderTotal);
  }

}

