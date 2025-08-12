package com.ecommerce.adapter.persistence.implementation;

import com.ecommerce.core.domain.user.entity.User;
import com.ecommerce.core.domain.user.repository.UserRepository;
import com.ecommerce.core.domain.user.valueobject.Email;
import com.ecommerce.core.domain.user.valueobject.UserStatus;
import com.ecommerce.adapter.persistence.jpa.repository.UserJpaRepository;
import com.ecommerce.adapter.persistence.jpa.entity.UserJpaEntity;
import com.ecommerce.adapter.persistence.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {
    
    private final UserJpaRepository userJpaRepository;
    private final UserMapper userMapper;

    public UserRepositoryImpl(UserJpaRepository userJpaRepository,
                             @Qualifier("persistenceUserMapper") UserMapper userMapper) {
        this.userJpaRepository = userJpaRepository;
        this.userMapper = userMapper;
    }

    @Override
    public User save(User user) {
        UserJpaEntity userEntity;

        if (user.getId() != null) {
            // Update existing user
            Optional<UserJpaEntity> existingEntity = userJpaRepository.findById(user.getId());
            if (existingEntity.isPresent()) {
                userEntity = existingEntity.get();
                userMapper.updateEntityFromDomain(user, userEntity);
            } else {
                // Create new entity if ID doesn't exist in database
                userEntity = userMapper.toEntity(user);
                userEntity.setId(null); // Let database generate ID
            }
        } else {
            // Create new user
            userEntity = userMapper.toEntity(user);
        }

        UserJpaEntity savedEntity = userJpaRepository.save(userEntity);
        return userMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userJpaRepository.findById(id)
                .map(userMapper::toDomain);
    }

    @Override
    public Optional<User> findByEmail(Email email) {
        return userJpaRepository.findByEmail(email.getValue())
                .map(userMapper::toDomain);
    }

    @Override
    public Optional<User> findByVerificationToken(String token) {
        // This method is not supported by the current UserJpaRepository
        // We would need to add this to the database schema and repository if needed
        return Optional.empty();
    }

    @Override
    public Optional<User> findByResetPasswordToken(String token) {
        // This method is not supported by the current UserJpaRepository
        // We would need to add this to the database schema and repository if needed
        return Optional.empty();
    }

    @Override
    public List<User> findByStatus(UserStatus status) {
        // Since the entity doesn't have status field, we approximate based on isActive
        if (status == UserStatus.ACTIVE) {
            return userJpaRepository.findByIsActiveTrue()
                    .stream()
                    .map(userMapper::toDomain)
                    .toList();
        } else {
            // For other statuses, return empty list as we can't differentiate
            return List.of();
        }
    }

    @Override
    public List<User> findAll() {
        return userJpaRepository.findAll()
                .stream()
                .map(userMapper::toDomain)
                .toList();
    }

    @Override
    public void delete(User user) {
        if (user.getId() != null) {
            userJpaRepository.deleteById(user.getId());
        }
    }

    @Override
    public void deleteById(Long id) {
        userJpaRepository.deleteById(id);
    }

    @Override
    public boolean existsByEmail(Email email) {
        return userJpaRepository.existsByEmail(email.getValue());
    }
    
    @Override
    public boolean existsById(Long id) {
        return userJpaRepository.existsById(id);
    }

    @Override
    public long count() {
        return userJpaRepository.count();
    }

    @Override
    public List<User> findActiveUsers() {
        return userJpaRepository.findByIsActiveTrue()
                .stream()
                .map(userMapper::toDomain)
                .toList();
    }

    @Override
    public List<User> findInactiveUsers() {
        // Get all users and filter out active ones
        return userJpaRepository.findAll()
                .stream()
                .filter(entity -> !entity.getIsActive())
                .map(userMapper::toDomain)
                .toList();
    }

    @Override
    public List<User> findUsersCreatedAfter(LocalDateTime date) {
        // This method is not supported by the current UserJpaRepository
        // We would need to add this query method if needed
        return userJpaRepository.findAll()
                .stream()
                .filter(entity -> entity.getCreatedAt().isAfter(date))
                .map(userMapper::toDomain)
                .toList();
    }
}