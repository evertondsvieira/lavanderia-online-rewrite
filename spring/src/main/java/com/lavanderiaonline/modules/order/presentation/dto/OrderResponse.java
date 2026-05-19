package com.lavanderiaonline.modules.order.presentation.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.lavanderiaonline.modules.order.domain.OrderStatus;
import com.lavanderiaonline.modules.orderItem.presentation.dto.OrderItemResponse;

public record OrderResponse(
  Long id,
  String orderNumber,
  OrderStatus status,
  LocalDateTime createdAt,
  LocalDateTime collectedAt,
  LocalDateTime washedAt,
  LocalDateTime paidAt,
  LocalDateTime finishedAt,
  LocalDateTime canceledAt,
  BigDecimal totalPrice,
  Integer deadlineDays,
  Long customerId,
  String customerName,
  List<OrderItemResponse> items
) {
}
