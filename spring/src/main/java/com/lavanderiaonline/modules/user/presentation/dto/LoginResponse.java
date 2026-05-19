package com.lavanderiaonline.modules.user.presentation.dto;

import com.lavanderiaonline.modules.user.domain.UserProfile;

public record LoginResponse(
  Long userId,
  String email,
  UserProfile profile,
  Long profileId,
  String name
) {
}
