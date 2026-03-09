package com.myproject.services.impl;

import com.myproject.exceptions.CartNotFoundException;
import com.myproject.exceptions.OutOfStockException;
import com.myproject.exceptions.ProductNotFoundException;
import com.myproject.models.dtos.*;
import com.myproject.models.entities.Cart;
import com.myproject.models.entities.CartItem;
import com.myproject.models.entities.Product;
import com.myproject.models.repositories.CartRepository;
import com.myproject.models.repositories.ProductRepository;
import com.myproject.services.interfaces.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public CartResponse addItemToCart(String userId, CartItemAddRequest request) {
        // Validate product exists and has sufficient stock
        Product product = productRepository.findById(request.getProductId())
            .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        if (product.getStock() < request.getQuantity()) {
            throw new OutOfStockException("Product out of stock");
        }

        // Get or create cart for user
        Cart cart = cartRepository.findByUserId(userId)
            .orElseGet(() -> {
                Cart newCart = new Cart();
                newCart.setUserId(userId);
                return cartRepository.save(newCart);
            });

        // Check if product already exists in cart
        Optional<CartItem> existingItem = cart.getItems().stream()
            .filter(item -> item.getProductId().equals(request.getProductId()))
            .findFirst();

        if (existingItem.isPresent()) {
            // Update quantity
            CartItem item = existingItem.get();
            int newQuantity = item.getQuantity() + request.getQuantity();
            
            if (product.getStock() < newQuantity) {
                throw new OutOfStockException("Insufficient stock for requested quantity");
            }
            
            item.setQuantity(newQuantity);
            item.calculateSubtotal();
        } else {
            // Add new item
            CartItem newItem = new CartItem();
            newItem.setProductId(product.getId());
            newItem.setProductName(product.getName());
            newItem.setPrice(product.getPrice());
            newItem.setQuantity(request.getQuantity());
            newItem.calculateSubtotal();
            cart.addItem(newItem);
        }

        cart.recalculateTotal();
        Cart savedCart = cartRepository.save(cart);
        return mapToCartResponse(savedCart);
    }

    @Override
    public CartResponse getCart(String userId) {
        Cart cart = cartRepository.findByUserId(userId)
            .orElseThrow(() -> new CartNotFoundException("Cart not found"));
        return mapToCartResponse(cart);
    }

    @Override
    public CartResponse removeItemFromCart(String userId, String productId) {
        Cart cart = cartRepository.findByUserId(userId)
            .orElseThrow(() -> new CartNotFoundException("Cart not found"));

        CartItem itemToRemove = cart.getItems().stream()
            .filter(item -> item.getProductId().equals(productId))
            .findFirst()
            .orElseThrow(() -> new ProductNotFoundException("Product not found in cart"));

        cart.removeItem(itemToRemove);
        cart.recalculateTotal();
        Cart savedCart = cartRepository.save(cart);
        return mapToCartResponse(savedCart);
    }

    @Override
    public CartResponse updateItemQuantity(String userId, String productId, CartItemUpdateRequest request) {
        Cart cart = cartRepository.findByUserId(userId)
            .orElseThrow(() -> new CartNotFoundException("Cart not found"));

        CartItem item = cart.getItems().stream()
            .filter(i -> i.getProductId().equals(productId))
            .findFirst()
            .orElseThrow(() -> new ProductNotFoundException("Product not found in cart"));

        // Validate stock
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        if (product.getStock() < request.getQuantity()) {
            throw new OutOfStockException("Requested quantity exceeds available stock");
        }

        item.setQuantity(request.getQuantity());
        item.calculateSubtotal();
        cart.recalculateTotal();
        Cart savedCart = cartRepository.save(cart);
        return mapToCartResponse(savedCart);
    }

    private CartResponse mapToCartResponse(Cart cart) {
        List<CartItemDTO> itemDTOs = cart.getItems().stream()
            .map(this::mapToCartItemDTO)
            .collect(Collectors.toList());

        CartResponse response = new CartResponse();
        response.setId(cart.getId());
        response.setUserId(cart.getUserId());
        response.setItems(itemDTOs);
        response.setTotalPrice(cart.getTotalPrice());
        response.setItemCount(cart.getItems().size());
        return response;
    }

    private CartItemDTO mapToCartItemDTO(CartItem item) {
        CartItemDTO dto = new CartItemDTO();
        dto.setId(item.getId());
        dto.setProductId(item.getProductId());
        dto.setProductName(item.getProductName());
        dto.setPrice(item.getPrice());
        dto.setQuantity(item.getQuantity());
        dto.setSubtotal(item.getSubtotal());
        return dto;
    }
}