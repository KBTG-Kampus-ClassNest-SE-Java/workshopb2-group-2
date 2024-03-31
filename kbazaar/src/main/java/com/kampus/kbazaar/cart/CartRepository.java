package com.kampus.kbazaar.cart;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    // JPQL
    @Query("SELECT c, ci FROM cart c INNER JOIN FETCH c.cartItems ci")
    List<Cart> findAllWithItems();

    Optional<Cart> findByUsername(String username);
}