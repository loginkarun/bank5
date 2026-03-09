package com.myproject.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myproject.models.dtos.CartItemAddRequest;
import com.myproject.models.dtos.CartItemDTO;
import com.myproject.models.dtos.CartItemUpdateRequest;
import com.myproject.models.dtos.CartResponse;
import com.myproject.services.interfaces.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CartService cartService;

    @Autowired
    private ObjectMapper objectMapper;

    private UUID userId;
    private UUID productId;
    private CartResponse mockCartResponse;

    @BeforeEach
    void setUp() {
        userId = UUID.fromString("987e6543-e21b-12d3-a456-426614174999");
        productId = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");

        mockCartResponse = new CartResponse();
        mockCartResponse.setId(UUID.randomUUID());
        mockCartResponse.setUserId(userId);
        mockCartResponse.setItems(new ArrayList<>());
        mockCartResponse.setTotalPrice(BigDecimal.ZERO);
        mockCartResponse.setItemCount(0);
    }

    @Test
    @WithMockUser
    void testAddItemToCart_Success() throws Exception {
        CartItemAddRequest request = new CartItemAddRequest(productId, 2);
        when(cartService.addItemToCart(any(UUID.class), any(CartItemAddRequest.class)))
                .thenReturn(mockCartResponse);

        mockMvc.perform(post("/api/cart/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").value(userId.toString()));
    }

    @Test
    @WithMockUser
    void testAddItemToCart_ValidationError() throws Exception {
        CartItemAddRequest request = new CartItemAddRequest(productId, 0); // Invalid quantity

        mockMvc.perform(post("/api/cart/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void testGetCart_Success() throws Exception {
        when(cartService.getCart(any(UUID.class))).thenReturn(mockCartResponse);

        mockMvc.perform(get("/api/cart"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(userId.toString()));
    }

    @Test
    @WithMockUser
    void testRemoveItemFromCart_Success() throws Exception {
        when(cartService.removeItemFromCart(any(UUID.class), eq(productId)))
                .thenReturn(mockCartResponse);

        mockMvc.perform(delete("/api/cart/{productId}", productId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(userId.toString()));
    }

    @Test
    @WithMockUser
    void testUpdateItemQuantity_Success() throws Exception {
        CartItemUpdateRequest request = new CartItemUpdateRequest(5);
        when(cartService.updateItemQuantity(any(UUID.class), eq(productId), any(CartItemUpdateRequest.class)))
                .thenReturn(mockCartResponse);

        mockMvc.perform(put("/api/cart/{productId}", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(userId.toString()));
    }
}
