package com.tui.proof.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.tui.proof.view.Views;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Table(name = "clients",
        uniqueConstraints = {
        @UniqueConstraint(name = "username_unique", columnNames = "username")
  }
)
@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Client {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @JsonView(Views.Public.class)
  private long clientId;

  @Size(max = 46, message = "firstName must be between 4 and 12 characters")
  private String firstName;

  @Size(max = 46, message = "lastName must be between 4 and 12 characters")
  private String lastName;

  @Size(min = 4, max = 12, message = "telephone must be between 4 and 12 characters")
  private String telephone;

  @Email(message = "email should be valid")
  @Size(max = 62, message = "email must be no longer than 62 characters")
  private String email;

  private String username;

  private String password;

  @Schema(hidden = true)
  @JsonIgnore
  private String role = "USER";

  @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
  @Schema(hidden = true)
  @JsonIgnore
  @ToString.Exclude
  private Set<Order> orders = new HashSet<>();

  public void setOrders(Set<Order> orders) {
    this.orders = orders;
    for (Order order : orders) {
      order.setClient(this);
    }
  }
}

