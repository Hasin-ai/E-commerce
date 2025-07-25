package com.ecommerce.adapter.persistence.implementation;

import com.ecommerce.core.domain.user.entity.User;
import com.ecommerce.core.domain.user.repository.UserRepository;
import com.ecommerce.core.domain.user.valueobject.Email;
import com.ecommerce.core.domain.user.valueobject.UserStatus;
import com.ecommerce.adapter.persistence.jpa.repository.UserJpaRepository;
import com.ecommerce.adapter.persistence.jpa.entity.UserJpaEntity;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class UserRepositoryImpl implements UserRepository {
    
    private final Map<Long, User> users = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);
    private final UserJpaRepository userJpaRepository;

    public UserRepositoryImpl(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public User save(User user) {
        if (user.getId() == null) {
            user.setId(idGenerator.getAndIncrement());
        }
        users.put(user.getId(), user);
        
        // Also save to database to satisfy foreign key constraints
        UserJpaEntity userEntity;
        Optional<UserJpaEntity> existingEntity = userJpaRepository.findById(user.getId());
        
        if (existingEntity.isPresent()) {
            // Update existing entity
            userEntity = existingEntity.get();
        } else {
            // Create new entity without setting ID (let JPA generate it)
            userEntity = new UserJpaEntity();
        }
        
        userEntity.setEmail(user.getEmail().getValue());
        userEntity.setPassword(user.getPassword().getValue());
        userEntity.setFirstName(user.getFirstName());
        userEntity.setLastName(user.getLastName());
        userEntity.setPhone(user.getPhone() != null ? user.getPhone().getValue() : null);
        userEntity.setIsActive(user.isActive());
        userEntity.setIsEmailVerified(user.isEmailVerified());
        userEntity.setCreatedAt(user.getCreatedAt());
        userEntity.setUpdatedAt(user.getUpdatedAt());
        
        UserJpaEntity savedEntity = userJpaRepository.save(userEntity);
        
        // Update the domain user ID with the generated ID if it was a new entity
        if (user.getId() == null || !existingEntity.isPresent()) {
            user.setId(savedEntity.getId());
            users.put(user.getId(), user);
        }
        
        return user;
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public Optional<User> findByEmail(Email email) {
        return users.values().stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst();
    }

    @Override
    public Optional<User> findByVerificationToken(String token) {
        return users.values().stream()
                .filter(user -> Objects.equals(user.getVerificationToken(), token))
                .findFirst();
    }

    @Override
    public Optional<User> findByResetPasswordToken(String token) {
        return users.values().stream()
                .filter(user -> Objects.equals(user.getResetPasswordToken(), token))
                .findFirst();
    }

    @Override
    public List<User> findByStatus(UserStatus status) {
        return users.values().stream()
                .filter(user -> user.getStatus() == status)
                .toList();
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public void delete(User user) {
        users.remove(user.getId());
    }

    @Override
    public void deleteById(Long id) {
        users.remove(id);
    }

    @Override
    public boolean existsByEmail(Email email) {
        return users.values().stream()
                .anyMatch(user -> user.getEmail().equals(email));
    }
    
    @Override
    public boolean existsById(Long id) {
        return users.containsKey(id);
    }

    @Override
    public long count() {
        return users.size();
    }

    @Override
    public List<User> findActiveUsers() {
        return findByStatus(UserStatus.ACTIVE);
    }

    @Override
    public List<User> findInactiveUsers() {
        return findByStatus(UserStatus.INACTIVE);
    }

    @Override
    public List<User> findUsersCreatedAfter(LocalDateTime date) {
        return users.values().stream()
                .filter(user -> user.getCreatedAt().isAfter(date))
                .toList();
    }
}