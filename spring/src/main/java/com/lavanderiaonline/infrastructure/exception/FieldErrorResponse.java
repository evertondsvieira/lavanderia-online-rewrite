package com.lavanderiaonline.infrastructure.exception;

public record FieldErrorResponse(
  String field,
  String message
) {
}
