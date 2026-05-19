package com.lavanderiaonline.modules.customer.presentation.dto;

import com.lavanderiaonline.modules.address.presentation.dto.AddressRequest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CustomerCreateRequest(
  @NotBlank
  @Size(max = 120)
  String name,

  @NotBlank
  @Size(min = 11, max = 11)
  @Pattern(regexp = "\\d{11}")
  String cpf,

  @NotBlank
  @Email
  @Size(max = 120)
  String email,

  @NotBlank
  @Size(min = 4, max = 60)
  String password,

  @NotBlank
  @Size(min = 10, max = 11)
  @Pattern(regexp = "\\d{10,11}")
  String phone,

  @NotNull
  @Valid
  AddressRequest address
) {
}
