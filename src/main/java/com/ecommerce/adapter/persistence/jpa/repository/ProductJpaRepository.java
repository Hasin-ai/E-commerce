package com.ecommerce.adapter.persistence.jpa.repository;

import com.ecommerce.adapter.persistence.jpa.entity.ProductJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductJpaRepository extends JpaRepository<ProductJpaEntity, Long> {

    Optional<ProductJpaEntity> findBySlug(String slug);

    Optional<ProductJpaEntity> findBySku(String sku);

    boolean existsBySlug(String slug);

    boolean existsBySku(String sku);

    List<ProductJpaEntity> findByIsActiveTrue();

    Page<ProductJpaEntity> findByIsActiveTrue(Pageable pageable);

    List<ProductJpaEntity> findByIsFeaturedTrue();

    Page<ProductJpaEntity> findByIsFeaturedTrue(Pageable pageable);

    List<ProductJpaEntity> findByNameContainingIgnoreCase(String name);

    @Query("SELECT p FROM ProductJpaEntity p WHERE p.isActive = true AND p.name LIKE %:name%")
    List<ProductJpaEntity> findActiveProductsByNameContaining(@Param("name") String name);

    @Query("SELECT p FROM ProductJpaEntity p JOIN p.categories c WHERE c.id = :categoryId")
    List<ProductJpaEntity> findByCategoryId(@Param("categoryId") Long categoryId);

    @Query("SELECT p FROM ProductJpaEntity p WHERE p.isActive = true AND p.basePrice BETWEEN :minPrice AND :maxPrice")
    List<ProductJpaEntity> findActiveProductsByPriceRange(@Param("minPrice") java.math.BigDecimal minPrice,
                                                          @Param("maxPrice") java.math.BigDecimal maxPrice);
}
