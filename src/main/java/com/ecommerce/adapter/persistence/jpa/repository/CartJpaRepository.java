package com.ecommerce.adapter.persistence.jpa.repository;

import com.ecommerce.adapter.persistence.jpa.entity.CartJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartJpaRepository extends JpaRepository<CartJpaEntity, Long> {

    Optional<CartJpaEntity> findByUserId(Long userId);

    void deleteByUserId(Long userId);
}