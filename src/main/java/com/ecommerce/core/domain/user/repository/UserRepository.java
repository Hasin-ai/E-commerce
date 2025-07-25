package com.ecommerce.core.domain.user.repository;

import com.ecommerce.core.domain.user.entity.User;
import com.ecommerce.core.domain.user.valueobject.Email;
import com.ecommerce.core.domain.user.valueobject.UserStatus;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    User save(User user);

    Optional<User> findById(Long id);

    Optional<User> findByEmail(Email email);

    Optional<User> findByVerificationToken(String token);

    Optional<User> findByResetPasswordToken(String token);

    List<User> findByStatus(UserStatus status);

    List<User> findAll();

    void delete(User user);

    void deleteById(Long id);

    boolean existsByEmail(Email email);
    
    boolean existsById(Long id);

    long count();

    List<User> findActiveUsers();

    List<User> findInactiveUsers();

    List<User> findUsersCreatedAfter(java.time.LocalDateTime date);
}
