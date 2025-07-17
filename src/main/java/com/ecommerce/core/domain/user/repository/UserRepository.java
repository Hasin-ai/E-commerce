package com.ecommerce.core.domain.user.repository;

import com.ecommerce.core.domain.user.entity.User;
import com.ecommerce.core.domain.user.valueobject.Email;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    User save(User user);

    Optional<User> findById(Long id);

    Optional<User> findByEmail(Email email);

    List<User> findAll();

    List<User> findActiveUsers();

    boolean existsByEmail(Email email);

    void deleteById(Long id);

    long count();

    // Pagination support
    List<User> findAll(int page, int size);

    List<User> findActiveUsers(int page, int size);
}
