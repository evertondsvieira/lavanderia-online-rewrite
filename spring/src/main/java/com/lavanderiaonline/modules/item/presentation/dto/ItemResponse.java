package com.lavanderiaonline.modules.item.presentation.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ItemResponse(
  Long id,
  String name,
  String description,
  BigDecimal unitPrice,
  Integer deadlineDays,
  String imageUrl,
  LocalDateTime deletedAt
) {
}
