package com.myproject.models.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for updating the quantity of an item in the cart.
 * Maps to CartItemUpdateRequest schema in OpenAPI.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemUpdateRequest {

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;
}
