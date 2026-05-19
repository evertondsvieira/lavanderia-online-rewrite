package com.lavanderiaonline.modules.orderItem.presentation.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import com.lavanderiaonline.modules.orderItem.domain.OrderItem;
import com.lavanderiaonline.modules.orderItem.presentation.dto.OrderItemRequest;
import com.lavanderiaonline.modules.orderItem.presentation.dto.OrderItemResponse;

@Mapper(
  componentModel = MappingConstants.ComponentModel.SPRING,
  unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface OrderItemMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "unitPrice", ignore = true)
  @Mapping(target = "subtotal", ignore = true)
  @Mapping(target = "deadlineDays", ignore = true)
  @Mapping(target = "order", ignore = true)
  @Mapping(target = "item", ignore = true)
  OrderItem toEntity(OrderItemRequest request);

  @Mapping(target = "itemId", source = "item.id")
  @Mapping(target = "itemName", source = "item.name")
  OrderItemResponse toResponse(OrderItem orderItem);
}
