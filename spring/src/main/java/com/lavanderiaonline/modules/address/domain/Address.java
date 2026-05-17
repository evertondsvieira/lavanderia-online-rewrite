package com.lavanderiaonline.modules.address.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "addresses")
@Getter
@Setter
@NoArgsConstructor
public class Address {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank
  @Size(min = 8, max = 8)
  @Pattern(regexp = "\\d{8}")
  @Column(name = "cep", length = 8, nullable = false)
  private String cep;

  @NotBlank
  @Size(max = 120)
  @Column(name = "street", length = 120, nullable = false)
  private String street;

  @NotBlank
  @Size(max = 8)
  @Column(name = "number", length = 8, nullable = false)
  private String number;

  @Size(max = 240)
  @Column(name = "complement", length = 240)
  private String complement;

  @NotBlank
  @Size(max = 120)
  @Column(name = "neighborhood", length = 120, nullable = false)
  private String neighborhood;

  @NotBlank
  @Size(max = 120)
  @Column(name = "city", length = 120, nullable = false)
  private String city;

  @NotBlank
  @Size(min = 2, max = 2)
  @Pattern(regexp = "[A-Z]{2}")
  @Column(name = "state", length = 2, nullable = false)
  private String state;
}