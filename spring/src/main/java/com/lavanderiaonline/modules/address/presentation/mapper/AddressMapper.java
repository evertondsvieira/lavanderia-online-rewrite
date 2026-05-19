package com.lavanderiaonline.modules.address.presentation.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import com.lavanderiaonline.modules.address.domain.Address;
import com.lavanderiaonline.modules.address.presentation.dto.AddressRequest;
import com.lavanderiaonline.modules.address.presentation.dto.AddressResponse;

@Mapper(
  componentModel = MappingConstants.ComponentModel.SPRING,
  unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface AddressMapper {
  
  @Mapping(target = "id", ignore = true)
  Address toEntity(AddressRequest request);

  AddressResponse toResponse(Address address);
}
