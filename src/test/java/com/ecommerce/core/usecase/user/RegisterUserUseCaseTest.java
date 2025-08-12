package com.ecommerce.core.usecase.user;

import com.ecommerce.core.domain.user.entity.User;
import com.ecommerce.core.domain.user.repository.UserRepository;
import com.ecommerce.core.domain.user.valueobject.UserRole;
import com.ecommerce.core.domain.user.valueobject.UserStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class RegisterUserUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private RegisterUserUseCase registerUserUseCase;

    private RegisterUserRequest request;

    @BeforeEach
    void setUp() {
        request = new RegisterUserRequest();
        request.setUsername("newuser");
        request.setEmail("newuser@example.com");
        request.setPassword("password123");
        request.setFirstName("John");
        request.setLastName("Doe");
    }

    @Test
    @DisplayName("Should register user successfully")
    void shouldRegisterUserSuccessfully() {
        // Given
        when(userRepository.findByUsername(request.getUsername())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        
        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setUsername(request.getUsername());
        savedUser.setEmail(request.getEmail());
        savedUser.setRole(UserRole.CUSTOMER);
        savedUser.setStatus(UserStatus.ACTIVE);
        
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // When
        RegisterUserResponse response = registerUserUseCase.execute(request);

        // Then
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals(1L, response.getUserId());
        verify(userRepository).findByUsername(request.getUsername());
        verify(userRepository).findByEmail(request.getEmail());
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("Should fail when username already exists")
    void shouldFailWhenUsernameAlreadyExists() {
        // Given
        User existingUser = new User();
        existingUser.setUsername(request.getUsername());
        when(userRepository.findByUsername(request.getUsername())).thenReturn(Optional.of(existingUser));

        // When
        RegisterUserResponse response = registerUserUseCase.execute(request);

        // Then
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertEquals("Username already exists", response.getErrorMessage());
        verify(userRepository).findByUsername(request.getUsername());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should fail when email already exists")
    void shouldFailWhenEmailAlreadyExists() {
        // Given
        when(userRepository.findByUsername(request.getUsername())).thenReturn(Optional.empty());
        
        User existingUser = new User();
        existingUser.setEmail(request.getEmail());
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(existingUser));

        // When
        RegisterUserResponse response = registerUserUseCase.execute(request);

        // Then
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertEquals("Email already exists", response.getErrorMessage());
        verify(userRepository).findByUsername(request.getUsername());
        verify(userRepository).findByEmail(request.getEmail());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should handle repository exception")
    void shouldHandleRepositoryException() {
        // Given
        when(userRepository.findByUsername(request.getUsername())).thenThrow(new RuntimeException("Database error"));

        // When
        RegisterUserResponse response = registerUserUseCase.execute(request);

        // Then
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertTrue(response.getErrorMessage().contains("error"));
        verify(userRepository).findByUsername(request.getUsername());
    }
}
