package com.lavanderiaonline.modules.customer.domain;

import com.lavanderiaonline.modules.address.domain.Address;
import com.lavanderiaonline.modules.user.domain.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="customers")
@Getter
@Setter
@NoArgsConstructor
public class Customer {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank
  @Size(max = 120)
  @Column(name = "name", length = 120, nullable = false)
  private String name;

  @NotBlank
  @Size(min = 11, max = 11)
  @Pattern(regexp = "\\d{11}")
  @Column(name = "cpf", length = 11, nullable = false, unique = true)
  private String cpf;

  @NotBlank
  @Size(min = 10, max = 11)
  @Pattern(regexp = "\\d{10,11}")
  @Column(name = "phone", length = 11, nullable = false)
  private String phone;

  @NotNull
  @OneToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_id", nullable = false, unique = true)
  private User user;

  @NotNull
  @OneToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "address_id", nullable = false, unique = true)
  private Address address;
}
