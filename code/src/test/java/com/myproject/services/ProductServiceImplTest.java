package com.myproject.services;

import com.myproject.exceptions.ProductNotFoundException;
import com.myproject.models.entities.Product;
import com.myproject.models.repositories.ProductRepository;
import com.myproject.services.impl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private UUID productId;
    private Product mockProduct;

    @BeforeEach
    void setUp() {
        productId = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
        mockProduct = new Product();
        mockProduct.setId(productId);
        mockProduct.setName("Wireless Mouse");
        mockProduct.setPrice(new BigDecimal("49.99"));
        mockProduct.setStock(100);
    }

    @Test
    void testGetProductById_Success() {
        when(productRepository.findById(productId)).thenReturn(Optional.of(mockProduct));

        Product result = productService.getProductById(productId);

        assertNotNull(result);
        assertEquals(productId, result.getId());
        assertEquals("Wireless Mouse", result.getName());
    }

    @Test
    void testGetProductById_NotFound_ThrowsException() {
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> {
            productService.getProductById(productId);
        });
    }

    @Test
    void testValidateStock_Success() {
        when(productRepository.findById(productId)).thenReturn(Optional.of(mockProduct));

        boolean result = productService.validateStock(productId, 50);

        assertTrue(result);
    }

    @Test
    void testValidateStock_InsufficientStock() {
        when(productRepository.findById(productId)).thenReturn(Optional.of(mockProduct));

        boolean result = productService.validateStock(productId, 200);

        assertFalse(result);
    }
}
