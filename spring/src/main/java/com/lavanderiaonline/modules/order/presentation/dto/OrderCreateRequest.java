package com.lavanderiaonline.modules.order.presentation.dto;

import java.util.List;

import com.lavanderiaonline.modules.orderItem.presentation.dto.OrderItemRequest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record OrderCreateRequest(
  @NotEmpty
  List<@NotNull @Valid OrderItemRequest> items
) {
}
