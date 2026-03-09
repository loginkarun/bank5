package com.myproject.services.interfaces;

import com.myproject.models.dtos.CartItemAddRequest;
import com.myproject.models.dtos.CartItemUpdateRequest;
import com.myproject.models.dtos.CartResponse;

import java.util.UUID;

/**
 * Service interface for cart operations.
 */
public interface CartService {

    /**
     * Add item to cart.
     * @param userId User ID
     * @param request Add item request
     * @return Cart response
     */
    CartResponse addItemToCart(UUID userId, CartItemAddRequest request);

    /**
     * Get cart for user.
     * @param userId User ID
     * @return Cart response
     */
    CartResponse getCart(UUID userId);

    /**
     * Remove item from cart.
     * @param userId User ID
     * @param productId Product ID
     * @return Cart response
     */
    CartResponse removeItemFromCart(UUID userId, UUID productId);

    /**
     * Update item quantity in cart.
     * @param userId User ID
     * @param productId Product ID
     * @param request Update request
     * @return Cart response
     */
    CartResponse updateItemQuantity(UUID userId, UUID productId, CartItemUpdateRequest request);
}
