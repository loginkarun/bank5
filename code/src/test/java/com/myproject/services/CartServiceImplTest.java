package com.myproject.services;

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
import com.myproject.services.impl.CartServiceImpl;
import com.myproject.services.interfaces.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceImplTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ProductService productService;

    @InjectMocks
    private CartServiceImpl cartService;

    private UUID userId;
    private UUID productId;
    private Product mockProduct;
    private Cart mockCart;

    @BeforeEach
    void setUp() {
        userId = UUID.fromString("987e6543-e21b-12d3-a456-426614174999");
        productId = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");

        mockProduct = new Product();
        mockProduct.setId(productId);
        mockProduct.setName("Wireless Mouse");
        mockProduct.setPrice(new BigDecimal("49.99"));
        mockProduct.setStock(100);

        mockCart = new Cart();
        mockCart.setId(UUID.randomUUID());
        mockCart.setUserId(userId);
        mockCart.setItems(new ArrayList<>());
        mockCart.setTotalPrice(BigDecimal.ZERO);
    }

    @Test
    void testAddItemToCart_NewCart_Success() {
        CartItemAddRequest request = new CartItemAddRequest(productId, 2);
        when(productService.getProductById(productId)).thenReturn(mockProduct);
        when(productService.validateStock(productId, 2)).thenReturn(true);
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.empty());
        when(cartRepository.save(any(Cart.class))).thenReturn(mockCart);

        CartResponse response = cartService.addItemToCart(userId, request);

        assertNotNull(response);
        verify(cartRepository, times(1)).save(any(Cart.class));
    }

    @Test
    void testAddItemToCart_OutOfStock_ThrowsException() {
        CartItemAddRequest request = new CartItemAddRequest(productId, 200);
        when(productService.getProductById(productId)).thenReturn(mockProduct);
        when(productService.validateStock(productId, 200)).thenReturn(false);

        assertThrows(OutOfStockException.class, () -> {
            cartService.addItemToCart(userId, request);
        });
    }

    @Test
    void testGetCart_Success() {
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(mockCart));

        CartResponse response = cartService.getCart(userId);

        assertNotNull(response);
        assertEquals(userId, response.getUserId());
    }

    @Test
    void testGetCart_NotFound_ThrowsException() {
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.empty());

        assertThrows(CartNotFoundException.class, () -> {
            cartService.getCart(userId);
        });
    }

    @Test
    void testRemoveItemFromCart_Success() {
        CartItem item = new CartItem();
        item.setId(UUID.randomUUID());
        item.setProductId(productId);
        item.setProductName("Wireless Mouse");
        item.setQuantity(2);
        item.setPrice(new BigDecimal("49.99"));
        item.calculateSubtotal();
        mockCart.addItem(item);

        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(mockCart));
        when(cartRepository.save(any(Cart.class))).thenReturn(mockCart);

        CartResponse response = cartService.removeItemFromCart(userId, productId);

        assertNotNull(response);
        verify(cartRepository, times(1)).save(any(Cart.class));
    }

    @Test
    void testUpdateItemQuantity_Success() {
        CartItem item = new CartItem();
        item.setId(UUID.randomUUID());
        item.setProductId(productId);
        item.setProductName("Wireless Mouse");
        item.setQuantity(2);
        item.setPrice(new BigDecimal("49.99"));
        item.calculateSubtotal();
        mockCart.addItem(item);

        CartItemUpdateRequest request = new CartItemUpdateRequest(5);
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(mockCart));
        when(productService.validateStock(productId, 5)).thenReturn(true);
        when(cartRepository.save(any(Cart.class))).thenReturn(mockCart);

        CartResponse response = cartService.updateItemQuantity(userId, productId, request);

        assertNotNull(response);
        verify(cartRepository, times(1)).save(any(Cart.class));
    }
}
