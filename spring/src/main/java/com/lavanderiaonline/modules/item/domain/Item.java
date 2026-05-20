package com.lavanderiaonline.modules.item.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.validator.constraints.URL;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "laundry_items")
@Getter
@Setter
@NoArgsConstructor
public class Item {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank
  @Size(max = 80)
  @Column(name = "name", length = 80, nullable = false)
  private String name;

  @Size(max = 240)
  @Column(name = "description", length = 240)
  private String description;

  @NotNull
  @DecimalMin(value = "0.01")
  @Digits(integer = 8, fraction = 2)
  @Column(name = "unit_price", nullable = false, precision = 10, scale = 2)
  private BigDecimal unitPrice;

  @NotNull
  @Min(1)
  @Column(name = "deadline_days", nullable = false)
  private Integer deadlineDays;

  @Size(max = 255)
  @URL
  @Column(name = "image_url", length = 255)
  private String imageUrl;

  @Column(name = "deleted_at")
  private LocalDateTime deletedAt;
}
