package com.myproject.services.impl;

import com.myproject.exceptions.CartNotFoundException;
import com.myproject.exceptions.OutOfStockException;
import com.myproject.exceptions.ProductNotFoundException;
import com.myproject.models.dtos.CartItemAddRequest;
import com.myproject.models.dtos.CartItemDTO;
import com.myproject.models.dtos.CartItemUpdateRequest;
import com.myproject.models.dtos.CartResponse;
import com.myproject.models.entities.Cart;
import com.myproject.models.entities.CartItem;
import com.myproject.models.entities.Product;
import com.myproject.models.repositories.CartRepository;
import com.myproject.services.interfaces.CartService;
import com.myproject.services.interfaces.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implementation of CartService.
 */
@Service
@Transactional
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final ProductService productService;

    @Autowired
    public CartServiceImpl(CartRepository cartRepository, ProductService productService) {
        this.cartRepository = cartRepository;
        this.productService = productService;
    }

    @Override
    public CartResponse addItemToCart(UUID userId, CartItemAddRequest request) {
        // Validate product exists and stock
        Product product = productService.getProductById(request.getProductId());
        if (!productService.validateStock(request.getProductId(), request.getQuantity())) {
            throw new OutOfStockException(request.getProductId(), request.getQuantity(), product.getStock());
        }

        // Get or create cart
        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUserId(userId);
                    return newCart;
                });

        // Check if item already exists in cart
        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProductId().equals(request.getProductId()))
                .findFirst();

        if (existingItem.isPresent()) {
            // Update quantity
            CartItem item = existingItem.get();
            int newQuantity = item.getQuantity() + request.getQuantity();
            if (!productService.validateStock(request.getProductId(), newQuantity)) {
                throw new OutOfStockException(request.getProductId(), newQuantity, product.getStock());
            }
            item.setQuantity(newQuantity);
            item.calculateSubtotal();
        } else {
            // Add new item
            CartItem newItem = new CartItem();
            newItem.setProductId(request.getProductId());
            newItem.setProductName(product.getName());
            newItem.setQuantity(request.getQuantity());
            newItem.setPrice(product.getPrice());
            newItem.calculateSubtotal();
            cart.addItem(newItem);
        }

        cart.recalculateTotalPrice();
        Cart savedCart = cartRepository.save(cart);
        return mapToCartResponse(savedCart);
    }

    @Override
    @Transactional(readOnly = true)
    public CartResponse getCart(UUID userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new CartNotFoundException(userId));
        return mapToCartResponse(cart);
    }

    @Override
    public CartResponse removeItemFromCart(UUID userId, UUID productId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new CartNotFoundException(userId));

        CartItem itemToRemove = cart.getItems().stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new ProductNotFoundException("Product not found in cart: " + productId));

        cart.removeItem(itemToRemove);
        cart.recalculateTotalPrice();
        Cart savedCart = cartRepository.save(cart);
        return mapToCartResponse(savedCart);
    }

    @Override
    public CartResponse updateItemQuantity(UUID userId, UUID productId, CartItemUpdateRequest request) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new CartNotFoundException(userId));

        CartItem item = cart.getItems().stream()
                .filter(i -> i.getProductId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new ProductNotFoundException("Product not found in cart: " + productId));

        // Validate stock
        if (!productService.validateStock(productId, request.getQuantity())) {
            Product product = productService.getProductById(productId);
            throw new OutOfStockException(productId, request.getQuantity(), product.getStock());
        }

        item.setQuantity(request.getQuantity());
        item.calculateSubtotal();
        cart.recalculateTotalPrice();
        Cart savedCart = cartRepository.save(cart);
        return mapToCartResponse(savedCart);
    }

    private CartResponse mapToCartResponse(Cart cart) {
        CartResponse response = new CartResponse();
        response.setId(cart.getId());
        response.setUserId(cart.getUserId());
        response.setTotalPrice(cart.getTotalPrice());
        response.setItemCount(cart.getItems().size());

        List<CartItemDTO> itemDTOs = cart.getItems().stream()
                .map(this::mapToCartItemDTO)
                .collect(Collectors.toList());
        response.setItems(itemDTOs);

        return response;
    }

    private CartItemDTO mapToCartItemDTO(CartItem item) {
        CartItemDTO dto = new CartItemDTO();
        dto.setId(item.getId());
        dto.setProductId(item.getProductId());
        dto.setProductName(item.getProductName());
        dto.setQuantity(item.getQuantity());
        dto.setPrice(item.getPrice());
        dto.setSubtotal(item.getSubtotal());
        return dto;
    }
}
