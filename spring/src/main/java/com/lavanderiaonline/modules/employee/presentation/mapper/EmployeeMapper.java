package com.lavanderiaonline.modules.employee.presentation.mapper;

import java.util.List;

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
  @Mapping(target = "deletedAt", source = "user.deletedAt")
  EmployeeResponse toResponse(Employee employee);

  List<EmployeeResponse> toResponseList(List<Employee> customer);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "user", ignore = true)
  void updateEntity(EmployeeUpdateRequest request, @MappingTarget Employee employee);
}
