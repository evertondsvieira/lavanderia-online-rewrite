package com.lavanderiaonline.modules.user.usecases;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import com.lavanderiaonline.infrastructure.security.JwtProperties;
import com.lavanderiaonline.modules.user.domain.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class TokenService {

  private final SecretKey secretKey;
  private final long expirationMinutes;

  public TokenService(JwtProperties jwtProperties) {
    this.secretKey = Keys.hmacShaKeyFor(jwtProperties.secret().getBytes(StandardCharsets.UTF_8));
    this.expirationMinutes = jwtProperties.expirationMinutes();
  }

  public String generate(User user) {
    Instant now = Instant.now();
    Instant expiration = now.plus(expirationMinutes, ChronoUnit.MINUTES);

    return Jwts.builder()
      .subject(user.getId().toString())
      .claim("email", user.getEmail())
      .claim("profile", user.getProfile().name())
      .issuedAt(Date.from(now))
      .expiration(Date.from(expiration))
      .signWith(secretKey)
      .compact();
  }

  public Long getUserId(String token) {
    Claims claims = Jwts.parser()
      .verifyWith(secretKey)
      .build()
      .parseSignedClaims(token)
      .getPayload();

    return Long.valueOf(claims.getSubject());
  }
}
