package com.lavanderiaonline.modules.item.usecases;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.lavanderiaonline.infrastructure.config.ReadTx;
import com.lavanderiaonline.infrastructure.config.UseCaseTx;
import com.lavanderiaonline.infrastructure.exception.ResourceAlreadyExistsException;
import com.lavanderiaonline.infrastructure.exception.ResourceNotFoundException;
import com.lavanderiaonline.modules.item.domain.Item;
import com.lavanderiaonline.modules.item.presentation.dto.ItemCreateRequest;
import com.lavanderiaonline.modules.item.presentation.dto.ItemResponse;
import com.lavanderiaonline.modules.item.presentation.dto.ItemUpdateRequest;
import com.lavanderiaonline.modules.item.presentation.mapper.ItemMapper;
import com.lavanderiaonline.modules.item.repository.ItemRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@ReadTx
public class ItemUseCases {
  
  private final ItemRepository repository;
  private final ItemMapper mapper;

  @UseCaseTx
  public ItemResponse create(ItemCreateRequest request) {
    if (repository.existsByNameIgnoreCaseAndDeletedAtIsNull(request.name())) {
      throw new ResourceAlreadyExistsException("There is already an item with that name.");
    }

    Item item = mapper.toEntity(request);
    Item savedItem = repository.save(item);

    return mapper.toResponse(savedItem);
  }

  public ItemResponse findById(Long id) {
    Item item = repository.findByIdAndDeletedAtIsNull(id)
      .orElseThrow(() -> new ResourceNotFoundException("Item not found."));

    return mapper.toResponse(item);
  }

  public List<ItemResponse> findAll() {
    List<Item> items = repository.findAllByDeletedAtIsNull();
    return mapper.toResponseList(items);
  }

  @UseCaseTx
  public ItemResponse update(Long id, ItemUpdateRequest request) {
    Item item = repository.findByIdAndDeletedAtIsNull(id)
      .orElseThrow(() -> new ResourceNotFoundException("Item not found."));

    if (repository.existsByNameIgnoreCaseAndIdNotAndDeletedAtIsNull(request.name(), id)) {
      throw new ResourceAlreadyExistsException("There is already an item with that name.");
    }

    mapper.updateEntity(request, item);
    Item updatedItem = repository.save(item);

    return mapper.toResponse(updatedItem);
  }

  @UseCaseTx
  public void remove(Long id) {
    Item item = repository.findByIdAndDeletedAtIsNull(id)
      .orElseThrow(() -> new ResourceNotFoundException("Item not found."));

    item.setDeletedAt(LocalDateTime.now());
    repository.save(item);
  }
} 
