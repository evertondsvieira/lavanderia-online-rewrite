package com.lavanderiaonline.infrastructure.email;

public record CustomerPasswordCreatedEvent(
  String email,
  String password
) {
}
