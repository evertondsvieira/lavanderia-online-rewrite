package com.lavanderiaonline.modules.orderItem.domain;

import java.math.BigDecimal;

import com.lavanderiaonline.modules.item.domain.Item;
import com.lavanderiaonline.modules.order.domain.Order;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
  name = "laundry_order_items",
  indexes = {
    @Index(name = "idx_laundry_order_items_order_id", columnList = "order_id"),
    @Index(name = "idx_laundry_order_items_item_id", columnList = "item_id")
  },
  uniqueConstraints = {
    @UniqueConstraint(
      name = "uk_laundry_order_items_order_item",
      columnNames = {"order_id", "item_id"}
    )
  }
)
@Getter
@Setter
@NoArgsConstructor
public class OrderItem {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  @Min(1)
  @Column(name = "quantity", nullable = false)
  private Integer quantity;

  @NotNull
  @DecimalMin(value = "0.01")
  @Digits(integer = 8, fraction = 2)
  @Column(name = "unit_price", nullable = false, precision = 10, scale = 2)
  private BigDecimal unitPrice;

  @NotNull
  @DecimalMin(value = "0.01")
  @Digits(integer = 8, fraction = 2)
  @Column(name = "subtotal", nullable = false, precision = 10, scale = 2)
  private BigDecimal subtotal;

  @NotNull
  @Min(1)
  @Column(name = "deadline_days", nullable = false)
  private Integer deadlineDays;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "order_id", nullable = false)
  private Order order;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "item_id", nullable = false)
  private Item item;

  @PrePersist
  @PreUpdate
  private void calculateSubtotal() {
    if (this.unitPrice != null && this.quantity != null) {
      this.subtotal = this.unitPrice.multiply(BigDecimal.valueOf(this.quantity));
    }
  }
}