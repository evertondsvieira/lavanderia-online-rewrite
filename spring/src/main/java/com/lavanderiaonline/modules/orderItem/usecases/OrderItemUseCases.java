package com.lavanderiaonline.modules.orderItem.usecases;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.lavanderiaonline.infrastructure.config.ReadTx;
import com.lavanderiaonline.infrastructure.config.UseCaseTx;
import com.lavanderiaonline.infrastructure.exception.ResourceNotFoundException;
import com.lavanderiaonline.modules.item.domain.Item;
import com.lavanderiaonline.modules.item.repository.ItemRepository;
import com.lavanderiaonline.modules.order.domain.Order;
import com.lavanderiaonline.modules.orderItem.domain.OrderItem;
import com.lavanderiaonline.modules.orderItem.presentation.dto.OrderItemQuote;
import com.lavanderiaonline.modules.orderItem.presentation.dto.OrderItemRequest;
import com.lavanderiaonline.modules.orderItem.repository.OrderItemRepository;

import lombok.RequiredArgsConstructor;

@Service
@ReadTx
@RequiredArgsConstructor
public class OrderItemUseCases {

  private final ItemRepository itemRepository;
  private final OrderItemRepository orderItemRepository;

  public OrderItemQuote quote(List<OrderItemRequest> itemRequests) {
    List<OrderItem> orderItems = new ArrayList<>();
    BigDecimal totalPrice = BigDecimal.ZERO;
    Integer deadlineDays = 0;
    Set<Long> itemIds = new HashSet<>();

    for (OrderItemRequest itemRequest : itemRequests) {
      if (!itemIds.add(itemRequest.itemId())) {
        throw new IllegalArgumentException("Order cannot contain duplicated items.");
      }

      Item item = itemRepository.findByIdAndDeletedAtIsNull(itemRequest.itemId())
        .orElseThrow(() -> new ResourceNotFoundException("Item not found."));

      OrderItem orderItem = new OrderItem();
      orderItem.setItem(item);
      orderItem.setUnitPrice(item.getUnitPrice());
      orderItem.setDeadlineDays(item.getDeadlineDays());
      orderItem.setQuantity(itemRequest.quantity());

      BigDecimal subtotal = item.getUnitPrice()
        .multiply(BigDecimal.valueOf(itemRequest.quantity()));

      orderItem.setSubtotal(subtotal);
      totalPrice = totalPrice.add(subtotal);
      deadlineDays = Math.max(deadlineDays, item.getDeadlineDays());

      orderItems.add(orderItem);
    }

    return new OrderItemQuote(totalPrice, deadlineDays, orderItems);
  }

  @UseCaseTx
  public List<OrderItem> saveAll(Order order, List<OrderItem> items) {
    items.forEach(orderItem -> orderItem.setOrder(order));

    return orderItemRepository.saveAll(items);
  }

  public List<OrderItem> findAllByOrderId(Long orderId) {
    return orderItemRepository.findAllByOrderIdOrderByIdAsc(orderId);
  }
}
