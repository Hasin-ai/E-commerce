package com.ecommerce.core.usecase.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetUserRequest {
    private Long userId;
    private String email;
    
    public static GetUserRequest byId(Long userId) {
        return GetUserRequest.builder()
            .userId(userId)
            .build();
    }
    
    public static GetUserRequest byEmail(String email) {
        return GetUserRequest.builder()
            .email(email)
            .build();
    }
}