package com.ecommerce.adapter.persistence.mapper;

import com.ecommerce.core.domain.product.valueobject.Price;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class PriceMapper {

    public BigDecimal priceToBigDecimal(Price price) {
        return price != null ? price.getAmount() : null;
    }

    public Price bigDecimalToPrice(BigDecimal bigDecimal) {
        // Assuming a default currency; this might need to be adjusted
        return bigDecimal != null ? new Price(bigDecimal, "USD") : null;
    }
}