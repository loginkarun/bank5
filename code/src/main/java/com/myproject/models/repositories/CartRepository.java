package com.myproject.models.repositories;

import com.myproject.models.entities.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for Cart entity.
 */
@Repository
public interface CartRepository extends JpaRepository<Cart, UUID> {

    /**
     * Find cart by user ID.
     * @param userId User ID
     * @return Optional Cart
     */
    Optional<Cart> findByUserId(UUID userId);

    /**
     * Check if cart exists for user.
     * @param userId User ID
     * @return true if exists
     */
    boolean existsByUserId(UUID userId);
}
