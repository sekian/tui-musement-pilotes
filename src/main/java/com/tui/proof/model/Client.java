package com.tui.proof.model;

import lombok.Data;

import javax.persistence.*;
@Table(name = "clients")
@Entity
@Data
public class Client {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private String ID;
  private String firstName;
  private String lastName;
  private String telephone;
}
