package com.lavanderiaonline.modules.customer.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lavanderiaonline.modules.customer.domain.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

  Optional<Customer> findByUserId(Long userId);

  boolean existsByCpf(String cpf);

  boolean existsByCpfAndIdNot(String cpf, Long id);
}
