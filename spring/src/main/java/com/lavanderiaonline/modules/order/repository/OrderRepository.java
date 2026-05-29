package com.lavanderiaonline.modules.order.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lavanderiaonline.modules.order.domain.Order;
import com.lavanderiaonline.modules.order.domain.OrderStatus;

public interface OrderRepository extends JpaRepository<Order, Long> {

  Optional<Order> findByIdAndCustomerUserId(Long id, Long userId);

  Optional<Order> findByOrderNumberAndCustomerUserId(String orderNumber, Long userId);

  @Query("""
    select o
      from LaundryOrder o
    where o.customer.user.id = :userId
    order by o.createdAt desc
    """)
  List<Order> listByCustomer(@Param("userId") Long userId);

  @Query("""
    select o
      from LaundryOrder o
    where o.customer.user.id = :userId
      and o.status = :status
    order by o.createdAt desc
    """)
  List<Order> listByCustomerAndStatus(
    @Param("userId") Long userId,
    @Param("status") OrderStatus status
  );

  @Query("""
    select o
      from LaundryOrder o
    where o.status = :status
      and o.collectedAt is null
    order by o.createdAt asc
    """)
  List<Order> listOpenForEmployee(@Param("status") OrderStatus status);

  @Query("""
    select o
      from LaundryOrder o
    where (:status is null or o.status = :status)
      and (:startDateTime is null or o.createdAt >= :startDateTime)
      and (:endDateTime is null or o.createdAt < :endDateTime)
    order by o.createdAt asc
    """)
  List<Order> listForEmployee(
    @Param("status") OrderStatus status,
    @Param("startDateTime") LocalDateTime startDateTime,
    @Param("endDateTime") LocalDateTime endDateTime
  );
}
