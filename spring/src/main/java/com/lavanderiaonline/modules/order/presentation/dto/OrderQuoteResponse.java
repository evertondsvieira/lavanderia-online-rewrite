package com.lavanderiaonline.modules.order.presentation.dto;

import java.math.BigDecimal;
import java.util.List;

import com.lavanderiaonline.modules.orderItem.presentation.dto.OrderItemResponse;

public record OrderQuoteResponse(
  BigDecimal totalPrice,
  Integer deadlineDays,
  List<OrderItemResponse> items
) {
}
