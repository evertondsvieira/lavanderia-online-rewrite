package com.lavanderiaonline.modules.employee.presentation.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record EmployeeResponse(
  Long id,
  String name,
  String email,
  LocalDate birthDate,
  LocalDateTime deletedAt
) {
}
