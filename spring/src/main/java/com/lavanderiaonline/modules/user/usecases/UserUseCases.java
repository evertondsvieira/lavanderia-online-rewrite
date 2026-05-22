package com.lavanderiaonline.modules.user.usecases;

import org.springframework.stereotype.Service;

import com.lavanderiaonline.infrastructure.config.ReadTx;
import com.lavanderiaonline.infrastructure.config.UseCaseTx;
import com.lavanderiaonline.modules.user.domain.User;
import com.lavanderiaonline.modules.user.domain.UserProfile;
import com.lavanderiaonline.modules.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@ReadTx
public class UserUseCases {

  private final UserRepository userRepository;
  private final PasswordService passwordService;

  @UseCaseTx
  public User create(String email, String password, UserProfile profile) {
    String salt = passwordService.generateSalt();

    User user = new User();
    user.setEmail(email);
    user.setSalt(salt);
    user.setPasswordHash(passwordService.hash(password, salt));
    user.setProfile(profile);

    return userRepository.save(user);
  }
}
