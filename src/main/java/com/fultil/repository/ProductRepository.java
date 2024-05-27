package com.fultil.repository;

import com.fultil.entity.Product;
import com.fultil.enums.ProductCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    boolean existsBySkuCode(String skuCode);

    Page<Product> findAllByCreatedBy(String createdBy, Pageable pageable);
    Page<Product> findAllByNameContainingIgnoreCase(String name, Pageable pageable);
    Page<Product> findAllByNameIgnoreCase(String name, Pageable pageable);
    List<Product> findAllByCategory(ProductCategory category);

    @Query("""
           SELECT product
           FROM Product product
           WHERE LOWER(product.name) = :name
           AND product.createdBy = :createdBy
           """)
    Page<Product> findProductByCreator(@Param("name") String name,
                                       @Param("createdBy") String createdBy,
                                       Pageable pageable);
}
