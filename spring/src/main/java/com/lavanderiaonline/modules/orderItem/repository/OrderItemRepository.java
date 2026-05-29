package com.lavanderiaonline.modules.orderItem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lavanderiaonline.modules.orderItem.domain.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
  
  List<OrderItem> findAllByOrderIdOrderByIdAsc(Long orderId);
}
