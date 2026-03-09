package com.myproject.config;

import com.myproject.models.entities.Product;
import com.myproject.models.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public void run(String... args) throws Exception {
        // Initialize sample products
        if (productRepository.count() == 0) {
            Product product1 = new Product();
            product1.setName("Wireless Mouse");
            product1.setPrice(BigDecimal.valueOf(49.99));
            product1.setStock(100);
            productRepository.save(product1);

            Product product2 = new Product();
            product2.setName("Mechanical Keyboard");
            product2.setPrice(BigDecimal.valueOf(129.99));
            product2.setStock(50);
            productRepository.save(product2);

            Product product3 = new Product();
            product3.setName("USB-C Cable");
            product3.setPrice(BigDecimal.valueOf(19.99));
            product3.setStock(200);
            productRepository.save(product3);

            Product product4 = new Product();
            product4.setName("Laptop Stand");
            product4.setPrice(BigDecimal.valueOf(79.99));
            product4.setStock(75);
            productRepository.save(product4);

            Product product5 = new Product();
            product5.setName("Webcam HD");
            product5.setPrice(BigDecimal.valueOf(89.99));
            product5.setStock(30);
            productRepository.save(product5);
        }
    }
}