package com.ecommerce.core.usecase.user;

import com.ecommerce.core.domain.user.entity.User;
import com.ecommerce.core.domain.user.repository.UserRepository;
import com.ecommerce.core.domain.user.valueobject.Email;
import com.ecommerce.core.domain.user.valueobject.Phone;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateUserUseCase {
    
    private final UserRepository userRepository;
    
    public UpdateUserResponse execute(UpdateUserRequest request) {
        User user = userRepository.findById(request.getUserId())
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        // Update fields if provided
        if (request.getFirstName() != null) {
            user.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null) {
            user.setLastName(request.getLastName());
        }
        if (request.getEmail() != null) {
            user.setEmail(new Email(request.getEmail()));
        }
        if (request.getPhone() != null && !request.getPhone().trim().isEmpty()) {
            user.setPhone(request.getPhone());
        }
        
        User updatedUser = userRepository.save(user);
        
        return UpdateUserResponse.builder()
            .id(updatedUser.getId())
            .firstName(updatedUser.getFirstName())
            .lastName(updatedUser.getLastName())
            .email(updatedUser.getEmail().getValue())
            .phone(updatedUser.getPhone() != null ? updatedUser.getPhone().getValue() : null)
            .isActive(updatedUser.isActive())
            .isEmailVerified(updatedUser.isEmailVerified())
            .createdAt(updatedUser.getCreatedAt())
            .updatedAt(updatedUser.getUpdatedAt())
            .message("User updated successfully")
            .build();
    }
}