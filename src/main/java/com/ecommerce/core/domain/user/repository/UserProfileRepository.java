package com.ecommerce.core.domain.user.repository;

import com.ecommerce.core.domain.user.entity.UserProfile;

import java.util.List;
import java.util.Optional;

public interface UserProfileRepository {

    UserProfile save(UserProfile userProfile);

    Optional<UserProfile> findById(Long id);

    Optional<UserProfile> findByUserId(Long userId);

    List<UserProfile> findAll();

    void delete(UserProfile userProfile);

    void deleteById(Long id);

    boolean existsByUserId(Long userId);
}
