package com.ecommerce.adapter.persistence.jpa;

import com.ecommerce.adapter.persistence.entity.VendorEntity;
import com.ecommerce.core.domain.vendor.entity.VendorStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VendorJpaRepository extends JpaRepository<VendorEntity, Long> {
    Optional<VendorEntity> findBySlug(String slug);
    Optional<VendorEntity> findByEmail(String email);
    List<VendorEntity> findByStatus(VendorStatus status);
    List<VendorEntity> findByNameContainingIgnoreCase(String name);
}