package com.lavanderiaonline.infrastructure.exception;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ResourceAlreadyExistsException.class)
  public ResponseEntity<ApiErrorResponse> handleResourceAlreadyExists(
    ResourceAlreadyExistsException exception,
    HttpServletRequest request
  ) {
    return buildResponse(HttpStatus.CONFLICT, exception.getMessage(), request);
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ApiErrorResponse> handleResourceNotFound(
    ResourceNotFoundException exception,
    HttpServletRequest request
  ) {
    return buildResponse(HttpStatus.NOT_FOUND, exception.getMessage(), request);
  }

  @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
  public ResponseEntity<ApiErrorResponse> handleBadRequest(
    RuntimeException exception,
    HttpServletRequest request
  ) {
    return buildResponse(HttpStatus.BAD_REQUEST, exception.getMessage(), request);
  }

  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<ApiErrorResponse> handleBadCredentials(
    BadCredentialsException exception,
    HttpServletRequest request
  ) {
    return buildResponse(HttpStatus.UNAUTHORIZED, exception.getMessage(), request);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiErrorResponse> handleValidation(
    MethodArgumentNotValidException exception,
    HttpServletRequest request
  ) {
    List<FieldErrorResponse> fields = exception.getBindingResult()
      .getFieldErrors()
      .stream()
      .map(error -> new FieldErrorResponse(error.getField(), error.getDefaultMessage()))
      .toList();

    return buildResponse(
      HttpStatus.BAD_REQUEST,
      "Request validation failed.",
      request,
      fields
    );
  }

  private ResponseEntity<ApiErrorResponse> buildResponse(
    HttpStatus status,
    String message,
    HttpServletRequest request
  ) {
    return buildResponse(status, message, request, List.of());
  }

  private ResponseEntity<ApiErrorResponse> buildResponse(
    HttpStatus status,
    String message,
    HttpServletRequest request,
    List<FieldErrorResponse> fields
  ) {
    ApiErrorResponse response = new ApiErrorResponse(
      LocalDateTime.now(),
      status.value(),
      status.getReasonPhrase(),
      message,
      request.getRequestURI(),
      fields
    );

    return ResponseEntity.status(status).body(response);
  }
}
