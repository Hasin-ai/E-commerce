package com.ecommerce.adapter.persistence.mapper;

import com.ecommerce.core.domain.user.entity.User;
import com.ecommerce.adapter.persistence.jpa.entity.UserJpaEntity;
import org.springframework.stereotype.Component;

@Component("persistenceUserMapper")
public class UserMapper {

    public UserJpaEntity toEntity(User user) {
        if (user == null) {
            return null;
        }

        UserJpaEntity entity = new UserJpaEntity();
        entity.setId(user.getId());
        entity.setEmail(user.getEmail().getValue());
        entity.setPassword(user.getPassword().getValue());
        entity.setFirstName(user.getFirstName());
        entity.setLastName(user.getLastName());
        entity.setPhone(user.getPhone() != null ? user.getPhone().getValue() : null);
        entity.setIsActive(user.isActive());
        entity.setIsEmailVerified(user.isEmailVerified());
        entity.setCreatedAt(user.getCreatedAt());
        entity.setUpdatedAt(user.getUpdatedAt());

        return entity;
    }

    public User toDomain(UserJpaEntity entity) {
        if (entity == null) {
            return null;
        }

        User user = new User(
            entity.getEmail(),
            entity.getFirstName(),
            entity.getLastName(),
            entity.getPhone()
        );

        user.setId(entity.getId());
        user.setEncodedPassword(entity.getPassword());
        user.setEmailVerified(entity.getIsEmailVerified());
        user.setCreatedAt(entity.getCreatedAt());
        user.setUpdatedAt(entity.getUpdatedAt());

        // Set active status based on entity
        if (entity.getIsActive()) {
            user.activateAccount();
        } else {
            user.suspendAccount();
        }

        return user;
    }

    public void updateEntityFromDomain(User user, UserJpaEntity entity) {
        if (user == null || entity == null) {
            return;
        }

        entity.setEmail(user.getEmail().getValue());
        entity.setPassword(user.getPassword().getValue());
        entity.setFirstName(user.getFirstName());
        entity.setLastName(user.getLastName());
        entity.setPhone(user.getPhone() != null ? user.getPhone().getValue() : null);
        entity.setIsActive(user.isActive());
        entity.setIsEmailVerified(user.isEmailVerified());
        entity.setUpdatedAt(user.getUpdatedAt());
    }
}
