package com.lavanderiaonline.modules.user.usecases;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import com.lavanderiaonline.infrastructure.config.ReadTx;
import com.lavanderiaonline.modules.customer.domain.Customer;
import com.lavanderiaonline.modules.customer.repository.CustomerRepository;
import com.lavanderiaonline.modules.employee.domain.Employee;
import com.lavanderiaonline.modules.employee.repository.EmployeeRepository;
import com.lavanderiaonline.modules.user.domain.User;
import com.lavanderiaonline.modules.user.domain.UserProfile;
import com.lavanderiaonline.modules.user.presentation.dto.LoginRequest;
import com.lavanderiaonline.modules.user.presentation.dto.LoginResponse;
import com.lavanderiaonline.modules.user.presentation.dto.ProfileData;
import com.lavanderiaonline.modules.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@ReadTx
@RequiredArgsConstructor
public class AuthUseCases {

  private final UserRepository userRepository;
  private final CustomerRepository customerRepository;
  private final EmployeeRepository employeeRepository;
  private final PasswordService passwordService;
  private final TokenService tokenService;

  public LoginResponse login(LoginRequest request) {
    User user = userRepository.findByEmailIgnoreCaseAndDeletedAtIsNull(request.email())
      .orElseThrow(() -> new BadCredentialsException("Invalid email or password."));

    if (!passwordService.matches(request.password(), user.getSalt(), user.getPasswordHash())) {
      throw new BadCredentialsException("Invalid email or password.");
    }

    ProfileData profileData = getProfileData(user);
    String token = tokenService.generate(user);

    return new LoginResponse(
      user.getId(),
      user.getEmail(),
      user.getProfile(),
      profileData.id(),
      profileData.name(),
      token,
      "Bearer"
    );
  }

  private ProfileData getProfileData(User user) {
    if (user.getProfile() == UserProfile.CUSTOMER) {
      Customer customer = customerRepository.findByUserId(user.getId())
        .orElseThrow(() -> new BadCredentialsException("Invalid user profile."));
      return new ProfileData(customer.getId(), customer.getName());
    }

    Employee employee = employeeRepository.findByUserId(user.getId())
      .orElseThrow(() -> new BadCredentialsException("Invalid user profile."));
    return new ProfileData(employee.getId(), employee.getName());
  }
}
