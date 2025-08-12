package com.ecommerce.adapter.persistence.jpa.repository;

import com.ecommerce.adapter.persistence.jpa.entity.UserJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserJpaRepository extends JpaRepository<UserJpaEntity, Long> {

    Optional<UserJpaEntity> findByEmail(String email);

    boolean existsByEmail(String email);

    List<UserJpaEntity> findByIsActiveTrue();

    Page<UserJpaEntity> findByIsActiveTrue(Pageable pageable);

    List<UserJpaEntity> findByIsEmailVerifiedTrue();

    @Query("SELECT u FROM UserJpaEntity u WHERE u.isActive = true AND u.isEmailVerified = true")
    List<UserJpaEntity> findActiveAndVerifiedUsers();

    @Query("SELECT u FROM UserJpaEntity u WHERE u.firstName LIKE %:name% OR u.lastName LIKE %:name%")
    List<UserJpaEntity> findByNameContaining(@Param("name") String name);

    @Query("SELECT COUNT(u) FROM UserJpaEntity u WHERE u.isActive = true")
    long countActiveUsers();

    @Query("SELECT COUNT(u) FROM UserJpaEntity u WHERE u.isEmailVerified = true")
    long countVerifiedUsers();
}
