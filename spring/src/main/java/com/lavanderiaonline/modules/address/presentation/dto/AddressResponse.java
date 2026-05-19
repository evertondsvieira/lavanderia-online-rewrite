package com.lavanderiaonline.modules.address.presentation.dto;

public record AddressResponse(
  Long id,
  String cep,
  String street,
  String number,
  String complement,
  String neighborhood,
  String city,
  String state
) {
}
