package com.myproject.services.interfaces;

import com.myproject.models.entities.Product;

import java.util.UUID;

/**
 * Service interface for product operations.
 */
public interface ProductService {

    /**
     * Get product by ID.
     * @param productId Product ID
     * @return Product
     */
    Product getProductById(UUID productId);

    /**
     * Validate product stock.
     * @param productId Product ID
     * @param requestedQuantity Requested quantity
     * @return true if stock is available
     */
    boolean validateStock(UUID productId, int requestedQuantity);
}
