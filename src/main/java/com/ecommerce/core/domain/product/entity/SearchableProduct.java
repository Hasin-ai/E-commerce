package com.ecommerce.core.domain.product.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SearchableProduct {
    private Long id;
    private String name;
    private String description;

    public SearchableProduct(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }
}
