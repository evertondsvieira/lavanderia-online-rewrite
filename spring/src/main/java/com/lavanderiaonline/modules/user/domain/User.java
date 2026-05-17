package com.lavanderiaonline.modules.user.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank
  @Email
  @Size(max = 120)
  @Column(name = "email", length = 120, nullable = false, unique = true)
  private String email;

  @NotBlank
  @Size(min = 64, max = 64)
  @Column(name = "password_hash", length = 64, nullable = false)
  private String passwordHash;

  @NotBlank
  @Size(min = 64, max = 64)
  @Column(name = "salt", length = 64, nullable = false)
  private String salt;

  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(name = "profile", length = 30, nullable = false)
  private UserProfile profile;

  @Column(name = "is_active", nullable = false)
  private boolean active = true;
}
