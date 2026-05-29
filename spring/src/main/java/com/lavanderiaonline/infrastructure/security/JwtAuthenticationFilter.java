package com.lavanderiaonline.infrastructure.security;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.lavanderiaonline.modules.user.domain.User;
import com.lavanderiaonline.modules.user.repository.UserRepository;
import com.lavanderiaonline.modules.user.usecases.TokenService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import io.jsonwebtoken.JwtException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private static final String BEARER_PREFIX = "Bearer ";

  private final TokenService tokenService;
  private final UserRepository userRepository;

  @Override
  protected void doFilterInternal(
    HttpServletRequest request,
    HttpServletResponse response,
    FilterChain filterChain
  ) throws ServletException, IOException {
    String authorization = request.getHeader("Authorization");

    if (authorization != null && authorization.startsWith(BEARER_PREFIX)) {
      try {
        String token = authorization.substring(BEARER_PREFIX.length());
        Long userId = tokenService.getUserId(token);

        User user = userRepository.findByIdAndDeletedAtIsNull(userId)
          .orElseThrow(() -> new IllegalArgumentException("Authenticated user was not found."));

        authenticate(user);
      } catch (JwtException | IllegalArgumentException exception) {
        response.sendError(HttpStatus.UNAUTHORIZED.value(), "Invalid authentication token.");
        return;
      }
    }

    filterChain.doFilter(request, response);
  }

  private void authenticate(User user) {
    AuthenticatedUser principal = new AuthenticatedUser(user.getId(), user.getEmail(), user.getProfile());
    SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + user.getProfile().name());
    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
      principal,
      null,
      List.of(authority)
    );

    SecurityContextHolder.getContext().setAuthentication(authentication);
  }
}
