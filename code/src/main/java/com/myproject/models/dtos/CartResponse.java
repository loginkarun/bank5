package com.myproject.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * DTO for cart response containing all cart details.
 * Maps to CartResponse schema in OpenAPI.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartResponse {

    private UUID id;
    private UUID userId;
    private List<CartItemDTO> items = new ArrayList<>();
    private BigDecimal totalPrice;
    private Integer itemCount;
}
