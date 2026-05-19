package com.lavanderiaonline.modules.address.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AddressRequest(
  @NotBlank
  @Size(min = 8, max = 8)
  @Pattern(regexp = "\\d{8}")
  String cep,

  @NotBlank
  @Size(max = 120)
  String street,

  @NotBlank
  @Size(max = 8)
  String number,

  @Size(max = 240)
  String complement,

  @NotBlank
  @Size(max = 120)
  String neighborhood,

  @NotBlank
  @Size(max = 120)
  String city,

  @NotBlank
  @Size(min = 2, max = 2)
  @Pattern(regexp = "[A-Z]{2}")
  String state
) {
}
