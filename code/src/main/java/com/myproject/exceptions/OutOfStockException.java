package com.myproject.exceptions;

import java.util.UUID;

/**
 * Exception thrown when requested quantity exceeds available stock.
 */
public class OutOfStockException extends RuntimeException {

    public OutOfStockException(UUID productId, int requested, int available) {
        super(String.format("Out of stock for product %s. Requested: %d, Available: %d", 
                productId, requested, available));
    }

    public OutOfStockException(String message) {
        super(message);
    }
}
