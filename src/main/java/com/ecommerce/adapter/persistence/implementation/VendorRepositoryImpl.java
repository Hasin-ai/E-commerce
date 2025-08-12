package com.ecommerce.adapter.persistence.implementation;

import com.ecommerce.adapter.persistence.entity.VendorEntity;
import com.ecommerce.adapter.persistence.jpa.VendorJpaRepository;
import com.ecommerce.adapter.persistence.mapper.VendorMapper;
import com.ecommerce.core.domain.vendor.entity.Vendor;
import com.ecommerce.core.domain.vendor.entity.VendorStatus;
import com.ecommerce.core.domain.vendor.repository.VendorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class VendorRepositoryImpl implements VendorRepository {
    
    private final VendorJpaRepository jpaRepository;
    private final VendorMapper mapper;
    
    @Override
    public Optional<Vendor> findById(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }
    
    @Override
    public Optional<Vendor> findBySlug(String slug) {
        return jpaRepository.findBySlug(slug).map(mapper::toDomain);
    }
    
    @Override
    public Optional<Vendor> findByEmail(String email) {
        return jpaRepository.findByEmail(email).map(mapper::toDomain);
    }
    
    @Override
    public List<Vendor> findByStatus(VendorStatus status) {
        return jpaRepository.findByStatus(status).stream()
                .map(mapper::toDomain)
                .toList();
    }
    
    @Override
    public List<Vendor> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .toList();
    }
    
    @Override
    public Vendor save(Vendor vendor) {
        VendorEntity entity = mapper.toEntity(vendor);
        VendorEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }
    
    @Override
    public void delete(Long id) {
        jpaRepository.deleteById(id);
    }
}