package com.lavanderiaonline.modules.order.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.lavanderiaonline.modules.customer.domain.Customer;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "LaundryOrder")
@Table(
  name = "laundry_orders",
  indexes = {
    @Index(name = "idx_laundry_orders_customer_id", columnList = "customer_id"),
    @Index(name = "idx_laundry_orders_status", columnList = "status")
  }
)
@Getter
@Setter
@NoArgsConstructor
public class Order {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank
  @Size(max = 20)
  @Column(name = "order_number", nullable = false, unique = true, length = 20)
  private String orderNumber;

  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false, length = 30)
  private OrderStatus status = OrderStatus.OPEN;

  @NotNull
  @PastOrPresent
  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @PastOrPresent
  @Column(name = "collected_at")
  private LocalDateTime collectedAt;

  @PastOrPresent
  @Column(name = "washed_at")
  private LocalDateTime washedAt;

  @PastOrPresent
  @Column(name = "paid_at")
  private LocalDateTime paidAt;

  @PastOrPresent
  @Column(name = "finished_at")
  private LocalDateTime finishedAt;

  @PastOrPresent
  @Column(name = "canceled_at")
  private LocalDateTime canceledAt;

  @NotNull
  @DecimalMin(value = "0.01")
  @Digits(integer = 8, fraction = 2)
  @Column(name = "total_price", nullable = false, precision = 10, scale = 2)
  private BigDecimal totalPrice;

  @NotNull
  @Min(1)
  @Column(name = "deadline_days", nullable = false)
  private Integer deadlineDays;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "customer_id", nullable = false)
  private Customer customer;

  @PrePersist
  private void prePersist() {
    if (this.createdAt == null) {
      this.createdAt = LocalDateTime.now();
    }

    if (this.status == null) {
      this.status = OrderStatus.OPEN;
    }
  }
}
