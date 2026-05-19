package com.lavanderiaonline.modules.orderItem.presentation.dto;

import java.math.BigDecimal;

public record OrderItemResponse(
  Long id,
  Long itemId,
  String itemName,
  Integer quantity,
  BigDecimal unitPrice,
  BigDecimal subtotal,
  Integer deadlineDays
) {
}
