package com.ecommerce.adapter.persistence.mapper;

import com.ecommerce.core.domain.product.valueobject.SKU;
import org.springframework.stereotype.Component;

@Component
public class SKUMapper {

    public String skuToString(SKU sku) {
        return sku != null ? sku.getValue() : null;
    }

    public SKU stringToSku(String sku) {
        return sku != null ? new SKU(sku) : null;
    }
}