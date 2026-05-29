package com.lavanderiaonline.modules.item.presentation.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.lavanderiaonline.modules.item.presentation.dto.ItemCreateRequest;
import com.lavanderiaonline.modules.item.presentation.dto.ItemResponse;
import com.lavanderiaonline.modules.item.presentation.dto.ItemUpdateRequest;
import com.lavanderiaonline.modules.item.usecases.ItemUseCases;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

  private final ItemUseCases useCases;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @PreAuthorize("hasRole('EMPLOYEE')")
  public ItemResponse create(@Valid @RequestBody ItemCreateRequest request) {
    return useCases.create(request);
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasRole('CUSTOMER') or hasRole('EMPLOYEE')")
  public ItemResponse findById(@PathVariable Long id) {
    return useCases.findById(id);
  }

  @GetMapping
  @PreAuthorize("hasRole('CUSTOMER') or hasRole('EMPLOYEE')")
  public List<ItemResponse> findAll() {
    return useCases.findAll();
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasRole('EMPLOYEE')")
  public ItemResponse update(
    @PathVariable Long id,
    @Valid @RequestBody ItemUpdateRequest request
  ) {
    return useCases.update(id, request);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize("hasRole('EMPLOYEE')")
  public void remove(@PathVariable Long id) {
    useCases.remove(id);
  }
}
