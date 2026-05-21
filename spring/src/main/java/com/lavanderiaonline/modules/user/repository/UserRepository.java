package com.lavanderiaonline.modules.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lavanderiaonline.modules.user.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByEmailIgnoreCaseAndDeletedAtIsNull(String email);

  Optional<User> findByIdAndDeletedAtIsNull(Long id);
}
