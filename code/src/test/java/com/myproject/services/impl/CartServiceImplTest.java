package com.myproject.services.impl;

import com.myproject.exceptions.CartNotFoundException;
import com.myproject.exceptions.OutOfStockException;
import com.myproject.exceptions.ProductNotFoundException;
import com.myproject.models.dtos.CartItemAddRequest;
import com.myproject.models.dtos.CartItemUpdateRequest;
import com.myproject.models.dtos.CartResponse;
import com.myproject.models.entities.Cart;
import com.myproject.models.entities.CartItem;
import com.myproject.models.entities.Product;
import com.myproject.models.repositories.CartRepository;
import com.myproject.models.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceImplTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private CartServiceImpl cartService;

    private Product testProduct;
    private Cart testCart;
    private String userId = "user123";

    @BeforeEach
    void setUp() {
        testProduct = new Product();
        testProduct.setId("prod123");
        testProduct.setName("Test Product");
        testProduct.setPrice(BigDecimal.valueOf(49.99));
        testProduct.setStock(10);

        testCart = new Cart();
        testCart.setId("cart123");
        testCart.setUserId(userId);
        testCart.setItems(new ArrayList<>());
        testCart.setTotalPrice(BigDecimal.ZERO);
    }

    @Test
    void testAddItemToCart_Success() {
        CartItemAddRequest request = new CartItemAddRequest("prod123", 2);
        
        when(productRepository.findById("prod123")).thenReturn(Optional.of(testProduct));
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(testCart));
        when(cartRepository.save(any(Cart.class))).thenReturn(testCart);

        CartResponse response = cartService.addItemToCart(userId, request);

        assertNotNull(response);
        assertEquals(userId, response.getUserId());
        verify(cartRepository, times(1)).save(any(Cart.class));
    }

    @Test
    void testAddItemToCart_ProductNotFound() {
        CartItemAddRequest request = new CartItemAddRequest("prod123", 2);
        
        when(productRepository.findById("prod123")).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> {
            cartService.addItemToCart(userId, request);
        });
    }

    @Test
    void testAddItemToCart_OutOfStock() {
        CartItemAddRequest request = new CartItemAddRequest("prod123", 20);
        
        when(productRepository.findById("prod123")).thenReturn(Optional.of(testProduct));

        assertThrows(OutOfStockException.class, () -> {
            cartService.addItemToCart(userId, request);
        });
    }

    @Test
    void testGetCart_Success() {
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(testCart));

        CartResponse response = cartService.getCart(userId);

        assertNotNull(response);
        assertEquals(userId, response.getUserId());
    }

    @Test
    void testGetCart_NotFound() {
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.empty());

        assertThrows(CartNotFoundException.class, () -> {
            cartService.getCart(userId);
        });
    }

    @Test
    void testRemoveItemFromCart_Success() {
        CartItem item = new CartItem();
        item.setId("item123");
        item.setProductId("prod123");
        item.setProductName("Test Product");
        item.setPrice(BigDecimal.valueOf(49.99));
        item.setQuantity(2);
        item.setSubtotal(BigDecimal.valueOf(99.98));
        item.setCart(testCart);
        testCart.getItems().add(item);

        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(testCart));
        when(cartRepository.save(any(Cart.class))).thenReturn(testCart);

        CartResponse response = cartService.removeItemFromCart(userId, "prod123");

        assertNotNull(response);
        verify(cartRepository, times(1)).save(any(Cart.class));
    }

    @Test
    void testUpdateItemQuantity_Success() {
        CartItem item = new CartItem();
        item.setId("item123");
        item.setProductId("prod123");
        item.setProductName("Test Product");
        item.setPrice(BigDecimal.valueOf(49.99));
        item.setQuantity(2);
        item.setSubtotal(BigDecimal.valueOf(99.98));
        item.setCart(testCart);
        testCart.getItems().add(item);

        CartItemUpdateRequest request = new CartItemUpdateRequest(5);

        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(testCart));
        when(productRepository.findById("prod123")).thenReturn(Optional.of(testProduct));
        when(cartRepository.save(any(Cart.class))).thenReturn(testCart);

        CartResponse response = cartService.updateItemQuantity(userId, "prod123", request);

        assertNotNull(response);
        verify(cartRepository, times(1)).save(any(Cart.class));
    }

    @Test
    void testUpdateItemQuantity_OutOfStock() {
        CartItem item = new CartItem();
        item.setId("item123");
        item.setProductId("prod123");
        item.setProductName("Test Product");
        item.setPrice(BigDecimal.valueOf(49.99));
        item.setQuantity(2);
        item.setSubtotal(BigDecimal.valueOf(99.98));
        item.setCart(testCart);
        testCart.getItems().add(item);

        CartItemUpdateRequest request = new CartItemUpdateRequest(20);

        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(testCart));
        when(productRepository.findById("prod123")).thenReturn(Optional.of(testProduct));

        assertThrows(OutOfStockException.class, () -> {
            cartService.updateItemQuantity(userId, "prod123", request);
        });
    }
}