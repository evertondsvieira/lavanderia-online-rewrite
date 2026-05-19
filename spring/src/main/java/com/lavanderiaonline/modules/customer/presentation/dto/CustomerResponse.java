package com.lavanderiaonline.modules.customer.presentation.dto;

import com.lavanderiaonline.modules.address.presentation.dto.AddressResponse;

public record CustomerResponse(
  Long id,
  String name,
  String cpf,
  String email,
  String phone,
  AddressResponse address
) {
}
