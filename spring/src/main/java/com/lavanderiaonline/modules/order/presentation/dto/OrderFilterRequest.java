package com.lavanderiaonline.modules.order.presentation.dto;

import java.time.LocalDate;

import com.lavanderiaonline.modules.order.domain.OrderStatus;

public record OrderFilterRequest(
  OrderStatus status,
  LocalDate startDate,
  LocalDate endDate
) {
}
