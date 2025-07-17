package com.ecommerce.core.domain.cart.entity;

import com.ecommerce.core.domain.user.entity.User;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class Cart {

    private Long id;
    private User user;
    private List<CartItem> items = new ArrayList<>();
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}