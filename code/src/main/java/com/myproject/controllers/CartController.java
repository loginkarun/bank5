package com.myproject.controllers;

import com.myproject.models.dtos.CartItemAddRequest;
import com.myproject.models.dtos.CartItemUpdateRequest;
import com.myproject.models.dtos.CartResponse;
import com.myproject.services.interfaces.CartService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<CartResponse> addItemToCart(
            @Valid @RequestBody CartItemAddRequest request,
            Authentication authentication) {
        String userId = authentication.getName();
        CartResponse response = cartService.addItemToCart(userId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<CartResponse> getCart(Authentication authentication) {
        String userId = authentication.getName();
        CartResponse response = cartService.getCart(userId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<CartResponse> removeItemFromCart(
            @PathVariable String productId,
            Authentication authentication) {
        String userId = authentication.getName();
        CartResponse response = cartService.removeItemFromCart(userId, productId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<CartResponse> updateItemQuantity(
            @PathVariable String productId,
            @Valid @RequestBody CartItemUpdateRequest request,
            Authentication authentication) {
        String userId = authentication.getName();
        CartResponse response = cartService.updateItemQuantity(userId, productId, request);
        return ResponseEntity.ok(response);
    }
}