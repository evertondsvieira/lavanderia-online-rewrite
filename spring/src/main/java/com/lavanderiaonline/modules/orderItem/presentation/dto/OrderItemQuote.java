package com.lavanderiaonline.modules.orderItem.presentation.dto;

import java.math.BigDecimal;
import java.util.List;

import com.lavanderiaonline.modules.orderItem.domain.OrderItem;

public record OrderItemQuote(
  BigDecimal totalPrice,
  Integer deadlineDays,
  List<OrderItem> items
) {
}
