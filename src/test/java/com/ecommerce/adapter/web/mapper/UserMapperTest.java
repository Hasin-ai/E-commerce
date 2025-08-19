package com.ecommerce.adapter.web.mapper;

import com.ecommerce.core.domain.user.entity.User;
import com.ecommerce.core.domain.user.valueobject.UserRole;
import com.ecommerce.core.domain.user.valueobject.UserStatus;
import com.ecommerce.adapter.web.dto.response.UserResponseDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private UserMapper userMapper;
    private User user;

    @BeforeEach
    void setUp() {
        userMapper = new UserMapper();
        
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setRole(UserRole.CUSTOMER);
        user.setStatus(UserStatus.ACTIVE);
    }

    @Test
    @DisplayName("Should map user entity to response dto")
    void shouldMapUserEntityToResponseDto() {
        // When
        UserResponseDto responseDto = userMapper.toResponseDto(user);

        // Then
        assertNotNull(responseDto);
        assertEquals(user.getId(), responseDto.getId());
        assertEquals(user.getUsername(), responseDto.getUsername());
        assertEquals(user.getEmail(), responseDto.getEmail());
        assertEquals(user.getFirstName(), responseDto.getFirstName());
        assertEquals(user.getLastName(), responseDto.getLastName());
        assertEquals(user.getRole().toString(), responseDto.getRole());
        assertEquals(user.getStatus().toString(), responseDto.getStatus());
    }

    @Test
    @DisplayName("Should handle null user entity")
    void shouldHandleNullUserEntity() {
        // When
        UserResponseDto responseDto = userMapper.toResponseDto(null);

        // Then
        assertNull(responseDto);
    }

    @Test
    @DisplayName("Should map user entity with null fields")
    void shouldMapUserEntityWithNullFields() {
        // Given
        User userWithNulls = new User();
        userWithNulls.setId(1L);
        userWithNulls.setUsername("testuser");
        userWithNulls.setEmail("test@example.com");
        // firstName and lastName are null

        // When
        UserResponseDto responseDto = userMapper.toResponseDto(userWithNulls);

        // Then
        assertNotNull(responseDto);
        assertEquals(1L, responseDto.getId());
        assertEquals("testuser", responseDto.getUsername());
        assertEquals("test@example.com", responseDto.getEmail());
        assertNull(responseDto.getFirstName());
        assertNull(responseDto.getLastName());
    }

    @Test
    @DisplayName("Should map full name correctly")
    void shouldMapFullNameCorrectly() {
        // When
        UserResponseDto responseDto = userMapper.toResponseDto(user);

        // Then
        assertEquals("John Doe", responseDto.getFullName());
    }

    @Test
    @DisplayName("Should handle partial name mapping")
    void shouldHandlePartialNameMapping() {
        // Given
        user.setLastName(null);

        // When
        UserResponseDto responseDto = userMapper.toResponseDto(user);

        // Then
        assertEquals("John", responseDto.getFullName());
    }
}
