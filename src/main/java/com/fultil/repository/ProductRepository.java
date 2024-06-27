package com.fultil.repository;

import com.fultil.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    boolean existsBySkuCode(String skuCode);

    Page<Product> findAllByCreatedBy(String createdBy, Pageable pageable);
    Page<Product> findAllByNameContainingIgnoreCase(String name, Pageable pageable);
    Optional<Product> findById(Long id);
    @Query(
            """
            SELECT p
            FROM Product p
            WHERE LOWER(p.category.name) = :category
            """)
    Page<Product> findAllByCategory(@Param("category") String category, Pageable pageable);

    @Query("""
           SELECT product
           FROM Product product
           WHERE LOWER(product.name) = :name
           AND product.createdBy = :createdBy
           """)
    Page<Product> findProductByCreator(String name, String createdBy, Pageable pageable);

}
