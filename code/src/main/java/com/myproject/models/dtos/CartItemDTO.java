package com.myproject.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * DTO for individual cart item.
 * Maps to CartItem schema in OpenAPI.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDTO {

    private UUID id;
    private UUID productId;
    private String productName;
    private Integer quantity;
    private BigDecimal price;
    private BigDecimal subtotal;
}
