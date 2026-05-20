package com.lavanderiaonline.modules.user.presentation.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import com.lavanderiaonline.modules.employee.presentation.dto.EmployeeUpdateRequest;
import com.lavanderiaonline.modules.user.domain.User;

@Mapper(
  componentModel = MappingConstants.ComponentModel.SPRING,
  unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface UserMapper {

  @BeanMapping(ignoreByDefault = true)
  @Mapping(target = "email", source = "email")
  void updateFromEmployee(EmployeeUpdateRequest request, @MappingTarget User user);
}
