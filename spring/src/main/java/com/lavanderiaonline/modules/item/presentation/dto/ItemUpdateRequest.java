package com.lavanderiaonline.modules.item.presentation.dto;

import java.math.BigDecimal;

import org.hibernate.validator.constraints.URL;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ItemUpdateRequest(
  @NotBlank
  @Size(max = 80)
  String name,

  @Size(max = 240)
  String description,

  @NotNull
  @DecimalMin("0.01")
  @Digits(integer = 8, fraction = 2)
  BigDecimal unitPrice,

  @NotNull
  @Min(1)
  Integer deadlineDays,

  @Size(max = 255)
  @URL
  String imageUrl,

  @NotNull
  Boolean active
) {
}
