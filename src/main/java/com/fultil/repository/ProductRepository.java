package com.fultil.repository;

import com.fultil.entity.Product;
import com.fultil.enums.ProductCategory;
import com.fultil.payload.response.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findAllByName(String name, Pageable pageable);
    List<Product> findAllByCategory(ProductCategory category);
}
