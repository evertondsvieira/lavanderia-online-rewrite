package com.lavanderiaonline.infrastructure.email;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {

  private final JavaMailSender mailSender;

  public void sendCustomerPassword(String to, String password) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(to);
    message.setSubject("Senha de acesso - LOL Lavanderia On-Line");
    message.setText("""
      Seu cadastro foi realizado com sucesso.

      Login: %s
      Senha: %s
      """.formatted(to, password));

    mailSender.send(message);
  }
}
