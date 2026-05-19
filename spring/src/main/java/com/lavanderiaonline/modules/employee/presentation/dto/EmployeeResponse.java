package com.lavanderiaonline.modules.employee.presentation.dto;

import java.time.LocalDate;

public record EmployeeResponse(
  Long id,
  String name,
  String email,
  LocalDate birthDate,
  boolean active
) {
}
