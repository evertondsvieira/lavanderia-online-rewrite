package com.lavanderiaonline.modules.order.usecases;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

import org.springframework.stereotype.Service;

import com.lavanderiaonline.infrastructure.config.ReadTx;
import com.lavanderiaonline.infrastructure.config.UseCaseTx;
import com.lavanderiaonline.infrastructure.exception.ResourceNotFoundException;
import com.lavanderiaonline.modules.customer.domain.Customer;
import com.lavanderiaonline.modules.customer.usecases.CustomerUseCases;
import com.lavanderiaonline.modules.order.domain.Order;
import com.lavanderiaonline.modules.order.domain.OrderStatus;
import com.lavanderiaonline.modules.order.presentation.dto.OrderCreateRequest;
import com.lavanderiaonline.modules.order.presentation.dto.OrderFilterRequest;
import com.lavanderiaonline.modules.order.presentation.dto.OrderQuoteResponse;
import com.lavanderiaonline.modules.order.presentation.dto.OrderResponse;
import com.lavanderiaonline.modules.order.presentation.dto.OrderSummaryResponse;
import com.lavanderiaonline.modules.order.presentation.mapper.OrderMapper;
import com.lavanderiaonline.modules.order.repository.OrderRepository;
import com.lavanderiaonline.modules.orderItem.domain.OrderItem;
import com.lavanderiaonline.modules.orderItem.presentation.dto.OrderItemQuote;
import com.lavanderiaonline.modules.orderItem.usecases.OrderItemUseCases;

import lombok.RequiredArgsConstructor;

@Service
@ReadTx
@RequiredArgsConstructor
public class OrderUseCases {

  private final OrderMapper mapper;
  private final OrderRepository orderRepository;
  private final CustomerUseCases customerUseCase;
  private final OrderItemUseCases orderItemUseCases;

  public OrderQuoteResponse quote(OrderCreateRequest request) {
    OrderItemQuote quote = orderItemUseCases.quote(request.items());

    return mapper.toQuoteResponse(
      quote.totalPrice(),
      quote.deadlineDays(),
      quote.items()
    );
  }

  @UseCaseTx
  public OrderResponse create(Long userId, OrderCreateRequest request) {
    return saveQuotedOrder(userId, request, OrderStatus.OPEN);
  }

  @UseCaseTx
  public OrderResponse approve(Long userId, OrderCreateRequest request) {
    return create(userId, request);
  }

  @UseCaseTx
  public OrderResponse reject(Long userId, OrderCreateRequest request) {
    return saveQuotedOrder(userId, request, OrderStatus.REJECTED);
  }

  private OrderResponse saveQuotedOrder(Long userId, OrderCreateRequest request, OrderStatus status) {
    Customer customer = customerUseCase.findByUserIdOrThrow(userId);
    OrderItemQuote quote = orderItemUseCases.quote(request.items());

    Order order = new Order();
    order.setCustomer(customer);
    order.setStatus(status);
    order.setOrderNumber(generateOrderNumber());
    order.setTotalPrice(quote.totalPrice());
    order.setDeadlineDays(quote.deadlineDays());

    Order savedOrder = orderRepository.save(order);
    List<OrderItem> savedItems = orderItemUseCases.saveAll(savedOrder, quote.items());

    return mapper.toResponse(savedOrder, savedItems);
  }

  public List<OrderSummaryResponse> listCustomerOpenOrders(Long userId) {
    List<Order> orders = orderRepository.listByCustomerAndStatus(
      userId,
      OrderStatus.OPEN
    );

    return mapper.toSummaryResponseList(orders);
  }

  public List<OrderSummaryResponse> listCustomerOrders(Long userId, OrderStatus status) {
    List<Order> orders = status == null
      ? orderRepository.listByCustomer(userId)
      : orderRepository.listByCustomerAndStatus(userId, status);

    return mapper.toSummaryResponseList(orders);
  }

  public OrderResponse findCustomerOrderByNumber(Long userId, String orderNumber) {
    Order order = orderRepository.findByOrderNumberAndCustomerUserId(orderNumber, userId)
        .orElseThrow(() -> new ResourceNotFoundException("Order not found."));
    List<OrderItem> items = orderItemUseCases.findAllByOrderId(order.getId());

    return mapper.toResponse(order, items);
  }

  public List<OrderSummaryResponse> listOpenOrdersForEmployee() {
    List<Order> orders = orderRepository.listOpenForEmployee(OrderStatus.OPEN);

    return mapper.toSummaryResponseList(orders);
  }

  public List<OrderSummaryResponse> listOrdersForEmployee(OrderFilterRequest filter) {
    OrderStatus status = filter == null ? null : filter.status();

    validateFilterPeriod(filter);

    LocalDateTime startDateTime = filter == null || filter.startDate() == null
      ? null
      : filter.startDate().atStartOfDay();
    LocalDateTime endDateTime = filter == null || filter.endDate() == null
      ? null
      : filter.endDate().plusDays(1).atStartOfDay();

    List<Order> orders = orderRepository.listForEmployee(
      status,
      startDateTime,
      endDateTime
    );

    return mapper.toSummaryResponseList(orders);
  }

  public List<OrderSummaryResponse> listTodayOrdersForEmployee() {
    LocalDate today = LocalDate.now();

    return listOrdersForEmployee(new OrderFilterRequest(null, today, today));
  }

  public OrderResponse findOrderByIdForEmployee(Long orderId) {
    Order order = findOrderOrThrow(orderId);
    List<OrderItem> items = orderItemUseCases.findAllByOrderId(order.getId());

    return mapper.toResponse(order, items);
  }

  @UseCaseTx
  public OrderResponse cancel(Long userId, Long orderId) {
    return updateStatus(
      userId,
      orderId,
      OrderStatus.OPEN,
      OrderStatus.CANCELED,
      "Only open orders can be canceled.",
      order -> order.setCanceledAt(LocalDateTime.now())
    );
  }

  @UseCaseTx
  public OrderResponse collect(Long orderId) {
    return updateEmployeeStatus(
      orderId,
      OrderStatus.OPEN,
      OrderStatus.COLLECTED,
      "Only open orders can be collected.",
      order -> order.setCollectedAt(LocalDateTime.now())
    );
  }

  @UseCaseTx
  public OrderResponse markAsWashed(Long orderId) {
    return updateEmployeeStatus(
      orderId,
      OrderStatus.COLLECTED,
      OrderStatus.WAITING_PAYMENT,
      "Only collected orders can be marked as washed.",
      order -> order.setWashedAt(LocalDateTime.now())
    );
  }

  @UseCaseTx
  public OrderResponse pay(Long userId, Long orderId) {
    return updateStatus(
      userId,
      orderId,
      OrderStatus.WAITING_PAYMENT,
      OrderStatus.PAID,
      "Only orders waiting for payment can be paid.",
      order -> order.setPaidAt(LocalDateTime.now())
    );
  }

  @UseCaseTx
  public OrderResponse finish(Long orderId) {
    return updateEmployeeStatus(
      orderId,
      OrderStatus.PAID,
      OrderStatus.FINISHED,
      "Only paid orders can be finished.",
      order -> order.setFinishedAt(LocalDateTime.now())
    );
  }

  private OrderResponse updateStatus(
    Long userId,
    Long orderId,
    OrderStatus requiredStatus,
    OrderStatus newStatus,
    String invalidStatusMessage,
    Consumer<Order> beforeSave
  ) {
    Order order = findCustomerOrderOrThrow(userId, orderId);

    if (order.getStatus() != requiredStatus) {
      throw new IllegalStateException(invalidStatusMessage);
    }

    order.setStatus(newStatus);
    beforeSave.accept(order);

    Order savedOrder = orderRepository.save(order);
    List<OrderItem> items = orderItemUseCases.findAllByOrderId(savedOrder.getId());

    return mapper.toResponse(savedOrder, items);
  }

  private OrderResponse updateEmployeeStatus(
    Long orderId,
    OrderStatus requiredStatus,
    OrderStatus newStatus,
    String invalidStatusMessage,
    Consumer<Order> beforeSave
  ) {
    Order order = findOrderOrThrow(orderId);

    if (order.getStatus() != requiredStatus) {
      throw new IllegalStateException(invalidStatusMessage);
    }

    order.setStatus(newStatus);
    beforeSave.accept(order);

    Order savedOrder = orderRepository.save(order);
    List<OrderItem> items = orderItemUseCases.findAllByOrderId(savedOrder.getId());

    return mapper.toResponse(savedOrder, items);
  }

  private void validateFilterPeriod(OrderFilterRequest filter) {
    if (
      filter != null
        && filter.startDate() != null
        && filter.endDate() != null
        && filter.startDate().isAfter(filter.endDate())
    ) {
      throw new IllegalArgumentException("Start date must be before or equal to end date.");
    }
  }

  private Order findCustomerOrderOrThrow(Long userId, Long orderId) {
    return orderRepository.findByIdAndCustomerUserId(orderId, userId)
      .orElseThrow(() -> new ResourceNotFoundException("Order not found."));
  }

  private Order findOrderOrThrow(Long orderId) {
    return orderRepository.findById(orderId)
      .orElseThrow(() -> new ResourceNotFoundException("Order not found."));
  }

  private String generateOrderNumber() {
    return "LOL" + UUID.randomUUID().toString().replace("-", "").substring(0, 17).toUpperCase();
  }
}
