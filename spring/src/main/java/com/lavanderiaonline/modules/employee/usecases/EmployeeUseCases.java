package com.lavanderiaonline.modules.employee.usecases;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.lavanderiaonline.infrastructure.config.UseCaseTx;
import com.lavanderiaonline.infrastructure.exception.ResourceAlreadyExistsException;
import com.lavanderiaonline.infrastructure.exception.ResourceNotFoundException;
import com.lavanderiaonline.modules.employee.domain.Employee;
import com.lavanderiaonline.modules.employee.presentation.dto.EmployeeCreateRequest;
import com.lavanderiaonline.modules.employee.presentation.dto.EmployeeResponse;
import com.lavanderiaonline.modules.employee.presentation.dto.EmployeeUpdateRequest;
import com.lavanderiaonline.modules.employee.presentation.mapper.EmployeeMapper;
import com.lavanderiaonline.modules.employee.repository.EmployeeRepository;
import com.lavanderiaonline.modules.user.domain.User;
import com.lavanderiaonline.modules.user.domain.UserProfile;
import com.lavanderiaonline.modules.user.repository.UserRepository;
import com.lavanderiaonline.modules.user.usecases.UserUseCases;

import lombok.RequiredArgsConstructor;

@Service
@UseCaseTx
@RequiredArgsConstructor
public class EmployeeUseCases {

  private final EmployeeMapper mapper;
  private final EmployeeRepository employeeRepository;
  private final UserUseCases userUseCases;
  private final UserRepository userRepository;

  @UseCaseTx
  public EmployeeResponse create(EmployeeCreateRequest request) {
    if (userRepository.existsByEmailIgnoreCase(request.email())) {
      throw new ResourceAlreadyExistsException("There is already a user with that email.");
    }

    User user = userUseCases.create(request.email(), request.password(), UserProfile.EMPLOYEE);
    Employee employee = mapper.toEntity(request);
    employee.setUser(user);

    Employee savedEmployee = employeeRepository.save(employee);

    return mapper.toResponse(savedEmployee);
  }

  public List<EmployeeResponse> findAll() {
    List<Employee> employees = employeeRepository.findAllByUserDeletedAtIsNull();
    return mapper.toResponseList(employees);
  }

  public EmployeeResponse findById(Long id) {
    return mapper.toResponse(findByIdOrThrow(id));
  }

  @UseCaseTx
  public EmployeeResponse update(Long id, EmployeeUpdateRequest request) {
    Employee employee = findByIdOrThrow(id);

    User user = employee.getUser();
    if (userRepository.existsByEmailIgnoreCaseAndIdNot(request.email(), user.getId())) {
      throw new ResourceAlreadyExistsException("There is already a user with that email.");
    }

    mapper.updateEntity(request, employee);
    user.setEmail(request.email());

    Employee updatedEmployee = employeeRepository.save(employee);

    return mapper.toResponse(updatedEmployee);
  }

  @UseCaseTx
  public void remove(Long id) {
    Employee employee = findByIdOrThrow(id);

    employee.getUser().setDeletedAt(LocalDateTime.now());
    employeeRepository.save(employee);
  }

  private Employee findByIdOrThrow(Long id) {
    return employeeRepository.findByIdAndUserDeletedAtIsNull(id)
        .orElseThrow(() -> new ResourceNotFoundException("Employee not found."));
  }
}
