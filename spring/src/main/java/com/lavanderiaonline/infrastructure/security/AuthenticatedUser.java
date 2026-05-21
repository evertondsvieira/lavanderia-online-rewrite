package com.lavanderiaonline.infrastructure.security;

import com.lavanderiaonline.modules.user.domain.UserProfile;

public record AuthenticatedUser(
  Long id,
  String email,
  UserProfile profile
) {
}
