package com.lavanderiaonline.modules.customer.presentation.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import com.lavanderiaonline.modules.address.presentation.mapper.AddressMapper;
import com.lavanderiaonline.modules.customer.domain.Customer;
import com.lavanderiaonline.modules.customer.presentation.dto.CustomerCreateRequest;
import com.lavanderiaonline.modules.customer.presentation.dto.CustomerResponse;
import com.lavanderiaonline.modules.customer.presentation.dto.CustomerUpdateRequest;

@Mapper(
  componentModel = MappingConstants.ComponentModel.SPRING,
  unmappedTargetPolicy = ReportingPolicy.ERROR,
  uses = AddressMapper.class
)
public interface CustomerMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "user", ignore = true)
  Customer toEntity(CustomerCreateRequest request);

  List<CustomerResponse> toResponseList(List<Customer> customer);

  @Mapping(target = "email", source = "user.email")
  CustomerResponse toResponse(Customer customer);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "user", ignore = true)
  @Mapping(target = "address", ignore = true)
  void updateEntity(CustomerUpdateRequest request, @MappingTarget Customer customer);
}
