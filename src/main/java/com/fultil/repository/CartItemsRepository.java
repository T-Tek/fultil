package com.fultil.repository;

import com.fultil.model.CartItems;
import com.fultil.model.Product;
import com.fultil.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemsRepository extends JpaRepository<CartItems, Long> {
}
