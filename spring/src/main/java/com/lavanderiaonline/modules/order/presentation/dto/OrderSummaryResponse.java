package com.lavanderiaonline.modules.order.presentation.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.lavanderiaonline.modules.order.domain.OrderStatus;

public record OrderSummaryResponse(
  Long id,
  String orderNumber,
  OrderStatus status,
  LocalDateTime createdAt,
  BigDecimal totalPrice,
  Integer deadlineDays,
  Long customerId,
  String customerName
) {
}
