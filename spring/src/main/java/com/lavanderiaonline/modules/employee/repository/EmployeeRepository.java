package com.lavanderiaonline.modules.employee.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lavanderiaonline.modules.employee.domain.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

  Optional<Employee> findByUserId(Long userId);

  List<Employee> findAllByUserDeletedAtIsNull();

  Optional<Employee> findByIdAndUserDeletedAtIsNull(Long id);
}
