package com.ecommerce.core.domain.user.repository;

import com.ecommerce.core.domain.user.entity.UserAddress;
import com.ecommerce.core.domain.user.valueobject.AddressType;

import java.util.List;
import java.util.Optional;

public interface UserAddressRepository {

    UserAddress save(UserAddress userAddress);

    Optional<UserAddress> findById(Long id);

    List<UserAddress> findByUserId(Long userId);

    Optional<UserAddress> findByUserIdAndType(Long userId, AddressType type);

    Optional<UserAddress> findDefaultByUserId(Long userId);

    List<UserAddress> findByUserIdAndIsDefault(Long userId, boolean isDefault);

    List<UserAddress> findAll();

    void delete(UserAddress userAddress);

    void deleteById(Long id);

    void deleteByUserId(Long userId);

    boolean existsByUserIdAndType(Long userId, AddressType type);

    long countByUserId(Long userId);
}
