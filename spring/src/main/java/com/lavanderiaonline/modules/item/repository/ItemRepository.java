package com.lavanderiaonline.modules.item.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lavanderiaonline.modules.item.domain.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {
  
  Optional<Item> findByIdAndDeletedAtIsNull(Long id);

  List<Item> findAllByDeletedAtIsNull();

  boolean existsByNameIgnoreCaseAndDeletedAtIsNull(String name);

  boolean existsByNameIgnoreCaseAndIdNotAndDeletedAtIsNull(String name, Long id);
}
