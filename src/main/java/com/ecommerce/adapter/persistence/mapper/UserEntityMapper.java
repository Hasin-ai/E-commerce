package com.ecommerce.adapter.persistence.mapper;

import com.ecommerce.adapter.persistence.jpa.entity.UserJpaEntity;
import com.ecommerce.core.domain.user.entity.User;
import com.ecommerce.core.domain.user.valueobject.Email;
import com.ecommerce.core.domain.user.valueobject.Password;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Component
public class UserEntityMapper {

    public UserJpaEntity toJpaEntity(User user) {
        UserJpaEntity jpaEntity = new UserJpaEntity();

        jpaEntity.setId(user.getId());
        jpaEntity.setFirstName(user.getFirstName());
        jpaEntity.setLastName(user.getLastName());
        jpaEntity.setEmail(user.getEmail().getValue());
        jpaEntity.setPassword(user.getPassword().getValue());
        jpaEntity.setPhone(user.getPhone());
        jpaEntity.setIsActive(user.isActive());
        jpaEntity.setIsEmailVerified(user.isEmailVerified());
        jpaEntity.setCreatedAt(user.getCreatedAt());
        jpaEntity.setUpdatedAt(user.getUpdatedAt());

        return jpaEntity;
    }

    public User toDomainEntity(UserJpaEntity jpaEntity) {
        try {
            Email email = new Email(jpaEntity.getEmail());
            Password password = new Password(jpaEntity.getPassword());

            User user = new User(
                jpaEntity.getFirstName(),
                jpaEntity.getLastName(),
                email,
                password,
                jpaEntity.getPhone()
            );

            // Set private fields using reflection
            setPrivateField(user, "id", jpaEntity.getId());
            setPrivateField(user, "isActive", jpaEntity.getIsActive());
            setPrivateField(user, "isEmailVerified", jpaEntity.getIsEmailVerified());
            setPrivateField(user, "createdAt", jpaEntity.getCreatedAt());
            setPrivateField(user, "updatedAt", jpaEntity.getUpdatedAt());

            return user;

        } catch (Exception e) {
            throw new RuntimeException("Error mapping JPA entity to domain entity", e);
        }
    }

    private void setPrivateField(Object object, String fieldName, Object value)
            throws NoSuchFieldException, IllegalAccessException {
        Field field = object.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(object, value);
    }
}
