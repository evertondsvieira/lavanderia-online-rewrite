package com.lavanderiaonline.modules.customer.presentation.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.lavanderiaonline.infrastructure.security.AuthenticatedUser;
import com.lavanderiaonline.modules.customer.presentation.dto.CustomerCreateRequest;
import com.lavanderiaonline.modules.customer.presentation.dto.CustomerResponse;
import com.lavanderiaonline.modules.customer.presentation.dto.CustomerUpdateRequest;
import com.lavanderiaonline.modules.customer.usecases.CustomerUseCases;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/customers")
public class CustomerController {

  private final CustomerUseCases useCases;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public CustomerResponse create(@Valid @RequestBody CustomerCreateRequest request) {
    return useCases.create(request);
  }

  @GetMapping("/me")
  @PreAuthorize("hasRole('CUSTOMER')")
  public CustomerResponse findMine(@AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
    return useCases.findMine(authenticatedUser.id());
  }

  @PutMapping("/me")
  @PreAuthorize("hasRole('CUSTOMER')")
  public CustomerResponse update(
    @AuthenticationPrincipal AuthenticatedUser authenticatedUser,
    @Valid @RequestBody CustomerUpdateRequest request
  ) {
    return useCases.updateMine(authenticatedUser.id(), request);
  }
}
