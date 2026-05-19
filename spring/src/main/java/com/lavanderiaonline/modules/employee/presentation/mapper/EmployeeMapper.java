package com.lavanderiaonline.modules.employee.presentation.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import com.lavanderiaonline.modules.employee.domain.Employee;
import com.lavanderiaonline.modules.employee.presentation.dto.EmployeeCreateRequest;
import com.lavanderiaonline.modules.employee.presentation.dto.EmployeeResponse;
import com.lavanderiaonline.modules.employee.presentation.dto.EmployeeUpdateRequest;
import com.lavanderiaonline.modules.user.presentation.mapper.UserMapper;

@Mapper(
  componentModel = MappingConstants.ComponentModel.SPRING,
  unmappedTargetPolicy = ReportingPolicy.ERROR,
  uses = UserMapper.class
)
public interface EmployeeMapper {
  
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "user", ignore = true)
  Employee toEntity(EmployeeCreateRequest request);

  @Mapping(target = "email", source = "user.email")
  @Mapping(target = "active", source = "user.active")
  EmployeeResponse toResponse(Employee employee);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "user", ignore = true)
  void upgradeEntity(EmployeeUpdateRequest request, @MappingTarget Employee employee);
}
