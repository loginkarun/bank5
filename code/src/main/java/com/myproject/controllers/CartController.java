package com.myproject.controllers;

import com.myproject.models.dtos.CartItemAddRequest;
import com.myproject.models.dtos.CartItemUpdateRequest;
import com.myproject.models.dtos.CartResponse;
import com.myproject.services.interfaces.CartService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * REST Controller for shopping cart operations.
 * Handles all cart-related HTTP requests.
 */
@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "http://localhost:4200")
public class CartController {

    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    /**
     * Add item to cart.
     * POST /api/cart/add
     */
    @PostMapping("/add")
    public ResponseEntity<CartResponse> addItemToCart(
            @Valid @RequestBody CartItemAddRequest request,
            Authentication authentication) {
        UUID userId = getUserIdFromAuthentication(authentication);
        CartResponse response = cartService.addItemToCart(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Get cart.
     * GET /api/cart
     */
    @GetMapping
    public ResponseEntity<CartResponse> getCart(Authentication authentication) {
        UUID userId = getUserIdFromAuthentication(authentication);
        CartResponse response = cartService.getCart(userId);
        return ResponseEntity.ok(response);
    }

    /**
     * Remove item from cart.
     * DELETE /api/cart/{productId}
     */
    @DeleteMapping("/{productId}")
    public ResponseEntity<CartResponse> removeItemFromCart(
            @PathVariable UUID productId,
            Authentication authentication) {
        UUID userId = getUserIdFromAuthentication(authentication);
        CartResponse response = cartService.removeItemFromCart(userId, productId);
        return ResponseEntity.ok(response);
    }

    /**
     * Update item quantity.
     * PUT /api/cart/{productId}
     */
    @PutMapping("/{productId}")
    public ResponseEntity<CartResponse> updateItemQuantity(
            @PathVariable UUID productId,
            @Valid @RequestBody CartItemUpdateRequest request,
            Authentication authentication) {
        UUID userId = getUserIdFromAuthentication(authentication);
        CartResponse response = cartService.updateItemQuantity(userId, productId, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Extract user ID from authentication.
     * In real implementation, this would extract from JWT token or session.
     * For demo purposes, using a mock user ID.
     */
    private UUID getUserIdFromAuthentication(Authentication authentication) {
        // TODO: Extract actual user ID from JWT token or session
        // For now, return a mock user ID for testing
        return UUID.fromString("987e6543-e21b-12d3-a456-426614174999");
    }
}
