package com.myproject.config;

import com.myproject.models.entities.Product;
import com.myproject.models.entities.User;
import com.myproject.models.repositories.ProductRepository;
import com.myproject.models.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Data initializer to populate sample data for testing.
 */
@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(ProductRepository productRepository, 
                                     UserRepository userRepository,
                                     PasswordEncoder passwordEncoder) {
        return args -> {
            // Create sample user
            User user = new User();
            user.setId(UUID.fromString("987e6543-e21b-12d3-a456-426614174999"));
            user.setUsername("testuser");
            user.setEmail("testuser@example.com");
            user.setPassword(passwordEncoder.encode("password123"));
            userRepository.save(user);

            // Create sample products
            Product product1 = new Product();
            product1.setId(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"));
            product1.setName("Wireless Mouse");
            product1.setPrice(new BigDecimal("49.99"));
            product1.setStock(100);
            productRepository.save(product1);

            Product product2 = new Product();
            product2.setId(UUID.fromString("550e8400-e29b-41d4-a716-446655440001"));
            product2.setName("Mechanical Keyboard");
            product2.setPrice(new BigDecimal("129.99"));
            product2.setStock(50);
            productRepository.save(product2);

            Product product3 = new Product();
            product3.setId(UUID.fromString("550e8400-e29b-41d4-a716-446655440002"));
            product3.setName("USB-C Hub");
            product3.setPrice(new BigDecimal("39.99"));
            product3.setStock(75);
            productRepository.save(product3);

            Product product4 = new Product();
            product4.setId(UUID.fromString("550e8400-e29b-41d4-a716-446655440003"));
            product4.setName("Laptop Stand");
            product4.setPrice(new BigDecimal("59.99"));
            product4.setStock(30);
            productRepository.save(product4);

            System.out.println("Sample data initialized successfully!");
        };
    }
}
