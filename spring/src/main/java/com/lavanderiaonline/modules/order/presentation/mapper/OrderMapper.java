package com.lavanderiaonline.modules.order.presentation.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import com.lavanderiaonline.modules.order.domain.Order;
import com.lavanderiaonline.modules.order.presentation.dto.OrderCreateRequest;
import com.lavanderiaonline.modules.order.presentation.dto.OrderQuoteResponse;
import com.lavanderiaonline.modules.order.presentation.dto.OrderResponse;
import com.lavanderiaonline.modules.order.presentation.dto.OrderSummaryResponse;
import com.lavanderiaonline.modules.orderItem.domain.OrderItem;
import com.lavanderiaonline.modules.orderItem.presentation.mapper.OrderItemMapper;

@Mapper(
  componentModel = MappingConstants.ComponentModel.SPRING,
  unmappedTargetPolicy = ReportingPolicy.ERROR,
  uses = OrderItemMapper.class
)
public interface OrderMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "orderNumber", ignore = true)
  @Mapping(target = "status", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "collectedAt", ignore = true)
  @Mapping(target = "washedAt", ignore = true)
  @Mapping(target = "paidAt", ignore = true)
  @Mapping(target = "finishedAt", ignore = true)
  @Mapping(target = "canceledAt", ignore = true)
  @Mapping(target = "totalPrice", ignore = true)
  @Mapping(target = "deadlineDays", ignore = true)
  @Mapping(target = "customer", ignore = true)
  Order toEntity(OrderCreateRequest request);

  @Mapping(target = "customerId", source = "order.customer.id")
  @Mapping(target = "customerName", source = "order.customer.name")
  @Mapping(target = "items", source = "items")
  OrderResponse toResponse(Order order, List<OrderItem> items);

  @Mapping(target = "customerId", source = "customer.id")
  @Mapping(target = "customerName", source = "customer.name")
  OrderSummaryResponse toSummaryResponse(Order order);

  List<OrderSummaryResponse> toSummaryResponseList(List<Order> orders);

  OrderQuoteResponse toQuoteResponse(
      BigDecimal totalPrice,
      Integer deadlineDays,
      List<OrderItem> items
  );
}
