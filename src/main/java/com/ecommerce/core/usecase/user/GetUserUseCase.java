package com.ecommerce.core.usecase.user;

import com.ecommerce.core.domain.user.entity.User;
import com.ecommerce.core.domain.user.repository.UserRepository;
import com.ecommerce.core.domain.user.valueobject.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetUserUseCase {
    
    private final UserRepository userRepository;
    
    public GetUserResponse execute(GetUserRequest request) {
        User user;
        
        if (request.getUserId() != null) {
            user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        } else if (request.getEmail() != null) {
            user = userRepository.findByEmail(new Email(request.getEmail()))
                .orElseThrow(() -> new RuntimeException("User not found"));
        } else {
            throw new IllegalArgumentException("Either userId or email must be provided");
        }
        
        return GetUserResponse.builder()
            .id(user.getId())
            .firstName(user.getFirstName())
            .lastName(user.getLastName())
            .email(user.getEmail().getValue())
            .phone(user.getPhone() != null ? user.getPhone().getValue() : null)
            .isActive(user.isActive())
            .isEmailVerified(user.isEmailVerified())
            .createdAt(user.getCreatedAt())
            .updatedAt(user.getUpdatedAt())
            .build();
    }
}