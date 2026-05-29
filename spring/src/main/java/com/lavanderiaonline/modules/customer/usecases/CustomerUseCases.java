package com.lavanderiaonline.modules.customer.usecases;

import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.lavanderiaonline.infrastructure.config.ReadTx;
import com.lavanderiaonline.infrastructure.config.UseCaseTx;
import com.lavanderiaonline.infrastructure.email.CustomerPasswordCreatedEvent;
import com.lavanderiaonline.infrastructure.exception.ResourceAlreadyExistsException;
import com.lavanderiaonline.infrastructure.exception.ResourceNotFoundException;
import com.lavanderiaonline.modules.address.presentation.mapper.AddressMapper;
import com.lavanderiaonline.modules.customer.domain.Customer;
import com.lavanderiaonline.modules.customer.presentation.dto.CustomerCreateRequest;
import com.lavanderiaonline.modules.customer.presentation.dto.CustomerResponse;
import com.lavanderiaonline.modules.customer.presentation.dto.CustomerUpdateRequest;
import com.lavanderiaonline.modules.customer.presentation.mapper.CustomerMapper;
import com.lavanderiaonline.modules.customer.repository.CustomerRepository;
import com.lavanderiaonline.modules.user.domain.User;
import com.lavanderiaonline.modules.user.domain.UserProfile;
import com.lavanderiaonline.modules.user.repository.UserRepository;
import com.lavanderiaonline.modules.user.usecases.PasswordService;
import com.lavanderiaonline.modules.user.usecases.UserUseCases;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@ReadTx
public class CustomerUseCases {
  
  private final CustomerRepository customerRepository;
  private final UserRepository userRepository;
  private final UserUseCases userUseCases;
  private final PasswordService passwordService;
  private final CustomerMapper mapper;
  private final AddressMapper addressMapper;
  private final ApplicationEventPublisher eventPublisher;

  @UseCaseTx
  public CustomerResponse create(CustomerCreateRequest request) {
    if (customerRepository.existsByCpf(request.cpf())) {
      throw new ResourceAlreadyExistsException("There is already a customer with that CPF.");
    }

    if (userRepository.existsByEmailIgnoreCase(request.email())) {
      throw new ResourceAlreadyExistsException("There is already a user with that email.");
    }

    String password = passwordService.generateNumericPassword();
    User user = userUseCases.create(request.email(), password, UserProfile.CUSTOMER);
    Customer customer = mapper.toEntity(request);
    customer.setUser(user);

    Customer savedCustomer = customerRepository.save(customer);
    eventPublisher.publishEvent(new CustomerPasswordCreatedEvent(request.email(), password));

    return mapper.toResponse(savedCustomer);
  }

  public CustomerResponse findMine(Long userId) {
    Customer customer = findByUserIdOrThrow(userId);

    return mapper.toResponse(customer);
  }

  public List<CustomerResponse> findAll() {
    List<Customer> customer = customerRepository.findAll();
    return mapper.toResponseList(customer);
  }

  @UseCaseTx
  public CustomerResponse updateMine(Long userId, CustomerUpdateRequest request) {
    Customer customer = findByUserIdOrThrow(userId);

    if (customerRepository.existsByCpfAndIdNot(request.cpf(), customer.getId())) {
      throw new ResourceAlreadyExistsException("There is already a customer with that CPF.");
    }

    User user = customer.getUser();
    if (userRepository.existsByEmailIgnoreCaseAndIdNot(request.email(), user.getId())) {
      throw new ResourceAlreadyExistsException("There is already a user with that email.");
    }

    mapper.updateEntity(request, customer);
    addressMapper.updateEntity(request.address(), customer.getAddress());
    user.setEmail(request.email());

    Customer updatedCustomer = customerRepository.save(customer);

    return mapper.toResponse(updatedCustomer);
  }

  public Customer findByUserIdOrThrow(Long userId) {
    return customerRepository.findByUserId(userId)
      .orElseThrow(() -> new ResourceNotFoundException("Customer not found."));
  }
}
