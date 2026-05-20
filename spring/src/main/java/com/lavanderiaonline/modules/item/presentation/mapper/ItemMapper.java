package com.lavanderiaonline.modules.item.presentation.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import com.lavanderiaonline.modules.item.domain.Item;
import com.lavanderiaonline.modules.item.presentation.dto.ItemCreateRequest;
import com.lavanderiaonline.modules.item.presentation.dto.ItemResponse;
import com.lavanderiaonline.modules.item.presentation.dto.ItemUpdateRequest;

@Mapper(
  componentModel = MappingConstants.ComponentModel.SPRING,
  unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface ItemMapper {
  
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "deletedAt", ignore = true)
  Item toEntity(ItemCreateRequest request);

  ItemResponse toResponse(Item item);

  List<ItemResponse> toResponseList(List<Item> items);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "deletedAt", ignore = true)
  void updateEntity(ItemUpdateRequest request, @MappingTarget Item item);
}
