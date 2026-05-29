package com.lavanderiaonline.infrastructure.email;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomerPasswordEmailListener {

  private final EmailService emailService;

  @Async
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void onCustomerPasswordCreated(CustomerPasswordCreatedEvent event) {
    emailService.sendCustomerPassword(event.email(), event.password());
  }
}
