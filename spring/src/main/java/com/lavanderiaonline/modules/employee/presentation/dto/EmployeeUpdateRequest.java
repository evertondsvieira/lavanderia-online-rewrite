package com.lavanderiaonline.modules.employee.presentation.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

public record EmployeeUpdateRequest(
  @NotBlank
  @Size(max = 120)
  String name,

  @NotBlank
  @Email
  @Size(max = 120)
  String email,

  @NotNull
  @Past
  LocalDate birthDate,

  @NotNull
  Boolean active
) {
}
