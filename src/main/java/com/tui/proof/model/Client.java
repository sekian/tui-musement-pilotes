package com.tui.proof.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

@Table(name = "clients")
@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Client {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Schema(hidden = true)
  private long clientId;

  @NotNull(message = "firstName cannot be null")
  @Size(max = 46, message = "firstName must be between 4 and 12 characters")
  private String firstName;
  @NotNull(message = "lastName cannot be null")
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
  private String role = "USER";

//  public Client(String username, String password, String role) {
//    this.username = username;
//    this.password = password;
//    this.role = role;
//  }
}

