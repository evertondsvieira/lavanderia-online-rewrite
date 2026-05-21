package com.lavanderiaonline.modules.user.presentation.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lavanderiaonline.modules.user.presentation.dto.LoginRequest;
import com.lavanderiaonline.modules.user.presentation.dto.LoginResponse;
import com.lavanderiaonline.modules.user.usecases.AuthUseCases;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

  private final AuthUseCases useCases;

  @PostMapping("/login")
  public LoginResponse login(@Valid @RequestBody LoginRequest request) {
    return useCases.login(request);
  }
}
