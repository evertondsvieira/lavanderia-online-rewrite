package com.lavanderiaonline.modules.order.presentation.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.lavanderiaonline.infrastructure.security.AuthenticatedUser;
import com.lavanderiaonline.modules.order.domain.OrderStatus;
import com.lavanderiaonline.modules.order.presentation.dto.OrderCreateRequest;
import com.lavanderiaonline.modules.order.presentation.dto.OrderFilterRequest;
import com.lavanderiaonline.modules.order.presentation.dto.OrderQuoteResponse;
import com.lavanderiaonline.modules.order.presentation.dto.OrderResponse;
import com.lavanderiaonline.modules.order.presentation.dto.OrderSummaryResponse;
import com.lavanderiaonline.modules.order.usecases.OrderUseCases;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

  private final OrderUseCases useCases;

  @PostMapping("/quote")
  @PreAuthorize("hasRole('CUSTOMER')")
  public OrderQuoteResponse quote(@Valid @RequestBody OrderCreateRequest request) {
    return useCases.quote(request);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @PreAuthorize("hasRole('CUSTOMER')")
  public OrderResponse approve(
    @AuthenticationPrincipal AuthenticatedUser authenticatedUser,
    @Valid @RequestBody OrderCreateRequest request
  ) {
    return useCases.approve(authenticatedUser.id(), request);
  }

  @PostMapping("/rejections")
  @ResponseStatus(HttpStatus.CREATED)
  @PreAuthorize("hasRole('CUSTOMER')")
  public OrderResponse reject(
    @AuthenticationPrincipal AuthenticatedUser authenticatedUser,
    @Valid @RequestBody OrderCreateRequest request
  ) {
    return useCases.reject(authenticatedUser.id(), request);
  }

  @GetMapping("/mine/open")
  @PreAuthorize("hasRole('CUSTOMER')")
  public List<OrderSummaryResponse> listMineOpen(
    @AuthenticationPrincipal AuthenticatedUser authenticatedUser
  ) {
    return useCases.listCustomerOpenOrders(authenticatedUser.id());
  }

  @GetMapping("/mine")
  @PreAuthorize("hasRole('CUSTOMER')")
  public List<OrderSummaryResponse> listMine(
    @AuthenticationPrincipal AuthenticatedUser authenticatedUser,
    @RequestParam(required = false) OrderStatus status
  ) {
    return useCases.listCustomerOrders(authenticatedUser.id(), status);
  }

  @GetMapping("/mine/{orderNumber}")
  @PreAuthorize("hasRole('CUSTOMER')")
  public OrderResponse findMineByNumber(
    @AuthenticationPrincipal AuthenticatedUser authenticatedUser,
    @PathVariable String orderNumber
  ) {
    return useCases.findCustomerOrderByNumber(authenticatedUser.id(), orderNumber);
  }

  @PatchMapping("/mine/{id}/cancel")
  @PreAuthorize("hasRole('CUSTOMER')")
  public OrderResponse cancel(
    @AuthenticationPrincipal AuthenticatedUser authenticatedUser,
    @PathVariable Long id
  ) {
    return useCases.cancel(authenticatedUser.id(), id);
  }

  @PatchMapping("/mine/{id}/pay")
  @PreAuthorize("hasRole('CUSTOMER')")
  public OrderResponse pay(
    @AuthenticationPrincipal AuthenticatedUser authenticatedUser,
    @PathVariable Long id
  ) {
    return useCases.pay(authenticatedUser.id(), id);
  }

  @GetMapping("/employee/open")
  @PreAuthorize("hasRole('EMPLOYEE')")
  public List<OrderSummaryResponse> listOpenForEmployee() {
    return useCases.listOpenOrdersForEmployee();
  }

  @GetMapping("/employee/today")
  @PreAuthorize("hasRole('EMPLOYEE')")
  public List<OrderSummaryResponse> listTodayForEmployee() {
    return useCases.listTodayOrdersForEmployee();
  }

  @PostMapping("/employee/search")
  @PreAuthorize("hasRole('EMPLOYEE')")
  public List<OrderSummaryResponse> listForEmployee(@RequestBody(required = false) OrderFilterRequest filter) {
    return useCases.listOrdersForEmployee(filter);
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasRole('EMPLOYEE')")
  public OrderResponse findByIdForEmployee(@PathVariable Long id) {
    return useCases.findOrderByIdForEmployee(id);
  }

  @PatchMapping("/{id}/collect")
  @PreAuthorize("hasRole('EMPLOYEE')")
  public OrderResponse collect(@PathVariable Long id) {
    return useCases.collect(id);
  }

  @PatchMapping("/{id}/wash")
  @PreAuthorize("hasRole('EMPLOYEE')")
  public OrderResponse markAsWashed(@PathVariable Long id) {
    return useCases.markAsWashed(id);
  }

  @PatchMapping("/{id}/finish")
  @PreAuthorize("hasRole('EMPLOYEE')")
  public OrderResponse finish(@PathVariable Long id) {
    return useCases.finish(id);
  }
}
