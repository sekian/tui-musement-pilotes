package com.tui.proof.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Table(name = "clients")
@Entity
@Data
public class Client {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private String ID;
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
}

