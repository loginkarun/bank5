package com.myproject.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myproject.models.dtos.CartItemAddRequest;
import com.myproject.models.dtos.CartItemUpdateRequest;
import com.myproject.models.dtos.CartResponse;
import com.myproject.services.interfaces.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CartController.class)
class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CartService cartService;

    @Autowired
    private ObjectMapper objectMapper;

    private CartResponse testCartResponse;

    @BeforeEach
    void setUp() {
        testCartResponse = new CartResponse();
        testCartResponse.setId("cart123");
        testCartResponse.setUserId("testuser");
        testCartResponse.setItems(new ArrayList<>());
        testCartResponse.setTotalPrice(BigDecimal.ZERO);
        testCartResponse.setItemCount(0);
    }

    @Test
    @WithMockUser(username = "testuser")
    void testAddItemToCart_Success() throws Exception {
        CartItemAddRequest request = new CartItemAddRequest("prod123", 2);
        
        when(cartService.addItemToCart(eq("testuser"), any(CartItemAddRequest.class)))
            .thenReturn(testCartResponse);

        mockMvc.perform(post("/api/cart/add")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("cart123"))
                .andExpect(jsonPath("$.userId").value("testuser"));
    }

    @Test
    @WithMockUser(username = "testuser")
    void testGetCart_Success() throws Exception {
        when(cartService.getCart("testuser")).thenReturn(testCartResponse);

        mockMvc.perform(get("/api/cart")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("cart123"))
                .andExpect(jsonPath("$.userId").value("testuser"));
    }

    @Test
    @WithMockUser(username = "testuser")
    void testRemoveItemFromCart_Success() throws Exception {
        when(cartService.removeItemFromCart("testuser", "prod123"))
            .thenReturn(testCartResponse);

        mockMvc.perform(delete("/api/cart/prod123")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("cart123"));
    }

    @Test
    @WithMockUser(username = "testuser")
    void testUpdateItemQuantity_Success() throws Exception {
        CartItemUpdateRequest request = new CartItemUpdateRequest(5);
        
        when(cartService.updateItemQuantity(eq("testuser"), eq("prod123"), any(CartItemUpdateRequest.class)))
            .thenReturn(testCartResponse);

        mockMvc.perform(put("/api/cart/prod123")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("cart123"));
    }

    @Test
    @WithMockUser(username = "testuser")
    void testAddItemToCart_ValidationError() throws Exception {
        CartItemAddRequest request = new CartItemAddRequest(null, -1);

        mockMvc.perform(post("/api/cart/add")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}