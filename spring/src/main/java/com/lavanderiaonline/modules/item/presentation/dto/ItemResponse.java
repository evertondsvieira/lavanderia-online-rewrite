package com.lavanderiaonline.modules.item.presentation.dto;

import java.math.BigDecimal;

public record ItemResponse(
  Long id,
  String name,
  String description,
  BigDecimal unitPrice,
  Integer deadlineDays,
  String imageUrl,
  boolean active
) {
}
