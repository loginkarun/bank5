package com.myproject.services.interfaces;

import com.myproject.models.dtos.CartItemAddRequest;
import com.myproject.models.dtos.CartItemUpdateRequest;
import com.myproject.models.dtos.CartResponse;

public interface CartService {
    CartResponse addItemToCart(String userId, CartItemAddRequest request);
    CartResponse getCart(String userId);
    CartResponse removeItemFromCart(String userId, String productId);
    CartResponse updateItemQuantity(String userId, String productId, CartItemUpdateRequest request);
}