package com.ecommerce.core.domain.vendor.repository;

import com.ecommerce.core.domain.vendor.entity.Vendor;
import com.ecommerce.core.domain.vendor.entity.VendorStatus;

import java.util.List;
import java.util.Optional;

public interface VendorRepository {
    Optional<Vendor> findById(Long id);
    Optional<Vendor> findBySlug(String slug);
    Optional<Vendor> findByEmail(String email);
    List<Vendor> findByStatus(VendorStatus status);
    List<Vendor> findAll();
    Vendor save(Vendor vendor);
    void delete(Long id);
}