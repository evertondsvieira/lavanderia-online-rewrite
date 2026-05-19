package com.lavanderiaonline.modules.customer.presentation.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import com.lavanderiaonline.modules.address.presentation.mapper.AddressMapper;
import com.lavanderiaonline.modules.customer.domain.Customer;
import com.lavanderiaonline.modules.customer.presentation.dto.CustomerCreateRequest;
import com.lavanderiaonline.modules.customer.presentation.dto.CustomerResponse;

@Mapper(
  componentModel = MappingConstants.ComponentModel.SPRING,
  unmappedTargetPolicy = ReportingPolicy.ERROR,
  uses = AddressMapper.class
)
public interface CustomerMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "user", ignore = true)
  Customer toEntity(CustomerCreateRequest request);

  @Mapping(target = "email", source = "user.email")
  CustomerResponse toResponse(Customer customer);
}
