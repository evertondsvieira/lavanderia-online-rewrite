package com.lavanderiaonline.modules.user.usecases;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HexFormat;

import org.springframework.stereotype.Service;

@Service
public class PasswordService {

  private static final String ALGORITHM = "SHA-256";
  private static final SecureRandom RANDOM = new SecureRandom();

  public String generateSalt() {
    byte[] salt = new byte[32];
    RANDOM.nextBytes(salt);
    return HexFormat.of().formatHex(salt);
  }

  public String hash(String password, String salt) {
    try {
      MessageDigest digest = MessageDigest.getInstance(ALGORITHM);
      byte[] hashed = digest.digest((salt + password).getBytes(StandardCharsets.UTF_8));
      return HexFormat.of().formatHex(hashed);
    } catch (NoSuchAlgorithmException exception) {
      throw new IllegalStateException("SHA-256 algorithm is not available.", exception);
    }
  }

  public boolean matches(String rawPassword, String salt, String expectedHash) {
    return hash(rawPassword, salt).equals(expectedHash);
  }
}
