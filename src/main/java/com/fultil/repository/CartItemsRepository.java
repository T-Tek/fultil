package com.fultil.repository;

import com.fultil.model.CartItems;
import com.fultil.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemsRepository extends JpaRepository<CartItems, Long> {
    Optional<CartItems> findByProduct(Product product);
    void deleteCartItemsByProduct(Product product);

}
