package com.myproject.services.impl;

import com.myproject.exceptions.ProductNotFoundException;
import com.myproject.models.entities.Product;
import com.myproject.models.repositories.ProductRepository;
import com.myproject.services.interfaces.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Implementation of ProductService.
 */
@Service
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product getProductById(UUID productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
    }

    @Override
    public boolean validateStock(UUID productId, int requestedQuantity) {
        Product product = getProductById(productId);
        return product.getStock() >= requestedQuantity;
    }
}
