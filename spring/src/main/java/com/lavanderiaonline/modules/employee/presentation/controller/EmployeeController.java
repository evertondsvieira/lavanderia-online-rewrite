package com.lavanderiaonline.modules.employee.presentation.controller;

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

import com.lavanderiaonline.modules.employee.presentation.dto.EmployeeCreateRequest;
import com.lavanderiaonline.modules.employee.presentation.dto.EmployeeResponse;
import com.lavanderiaonline.modules.employee.presentation.dto.EmployeeUpdateRequest;
import com.lavanderiaonline.modules.employee.usecases.EmployeeUseCases;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/employees")
@PreAuthorize("hasRole('EMPLOYEE')")
public class EmployeeController {

  private final EmployeeUseCases useCases;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public EmployeeResponse create(@Valid @RequestBody EmployeeCreateRequest request) {
    return useCases.create(request);
  }

  @GetMapping("/{id}")
  public EmployeeResponse findById(@PathVariable Long id) {
    return useCases.findById(id);
  }

  @GetMapping
  public List<EmployeeResponse> findAll() {
    return useCases.findAll();
  }

  @PutMapping("/{id}")
  public EmployeeResponse update(
    @PathVariable Long id,
    @Valid @RequestBody EmployeeUpdateRequest request
  ) {
    return useCases.update(id, request);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void remove(@PathVariable Long id) {
    useCases.remove(id);
  }
}
