package com.ecommerce.adapter.persistence.implementation;

import com.ecommerce.adapter.persistence.jpa.entity.UserJpaEntity;
import com.ecommerce.adapter.persistence.jpa.repository.UserJpaRepository;
import com.ecommerce.adapter.persistence.mapper.UserEntityMapper;
import com.ecommerce.core.domain.user.entity.User;
import com.ecommerce.core.domain.user.repository.UserRepository;
import com.ecommerce.core.domain.user.valueobject.Email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;
    private final UserEntityMapper userEntityMapper;

    @Autowired
    public UserRepositoryImpl(UserJpaRepository userJpaRepository,
                             UserEntityMapper userEntityMapper) {
        this.userJpaRepository = userJpaRepository;
        this.userEntityMapper = userEntityMapper;
    }

    @Override
    public User save(User user) {
        UserJpaEntity jpaEntity = userEntityMapper.toJpaEntity(user);
        UserJpaEntity savedEntity = userJpaRepository.save(jpaEntity);
        return userEntityMapper.toDomainEntity(savedEntity);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userJpaRepository.findById(id)
            .map(userEntityMapper::toDomainEntity);
    }

    @Override
    public Optional<User> findByEmail(Email email) {
        return userJpaRepository.findByEmail(email.getValue())
            .map(userEntityMapper::toDomainEntity);
    }

    @Override
    public List<User> findAll() {
        return userJpaRepository.findAll()
            .stream()
            .map(userEntityMapper::toDomainEntity)
            .collect(Collectors.toList());
    }

    @Override
    public List<User> findActiveUsers() {
        return userJpaRepository.findByIsActiveTrue()
            .stream()
            .map(userEntityMapper::toDomainEntity)
            .collect(Collectors.toList());
    }

    @Override
    public boolean existsByEmail(Email email) {
        return userJpaRepository.existsByEmail(email.getValue());
    }

    @Override
    public void deleteById(Long id) {
        userJpaRepository.deleteById(id);
    }

    @Override
    public long count() {
        return userJpaRepository.count();
    }

    @Override
    public List<User> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userJpaRepository.findAll(pageable)
            .stream()
            .map(userEntityMapper::toDomainEntity)
            .collect(Collectors.toList());
    }

    @Override
    public List<User> findActiveUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userJpaRepository.findByIsActiveTrue(pageable)
            .stream()
            .map(userEntityMapper::toDomainEntity)
            .collect(Collectors.toList());
    }
}
