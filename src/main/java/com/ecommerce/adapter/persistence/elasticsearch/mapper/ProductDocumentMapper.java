package com.ecommerce.adapter.persistence.elasticsearch.mapper;

import com.ecommerce.adapter.persistence.elasticsearch.document.ProductDocument;
import com.ecommerce.core.domain.product.entity.Product;
import com.ecommerce.core.domain.product.valueobject.Price;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductDocumentMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "sku", target = "sku")
    @Mapping(target = "price", expression = "java(mapPriceToBigDecimal(product.getPrice()))")
    @Mapping(source = "stockQuantity", target = "stockQuantity")
    @Mapping(target = "categoryId", expression = "java(product.getCategoryId() != null ? product.getCategoryId().toString() : null)")
    @Mapping(target = "categoryName", expression = "java(getCategoryNameFromId(product.getCategoryId()))")
    @Mapping(target = "vendorId", ignore = true)
    @Mapping(target = "vendorName", ignore = true)
    @Mapping(source = "brand", target = "brand")
    @Mapping(source = "active", target = "active")
    @Mapping(target = "rating", constant = "0.0")
    @Mapping(target = "reviewCount", constant = "0")
    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(source = "updatedAt", target = "updatedAt")
    @Mapping(target = "tags", expression = "java(generateTagsFromProduct(product))")
    ProductDocument toDocument(Product product);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "sku", target = "sku")
    @Mapping(target = "price", expression = "java(createPriceFromAmount(document.getPrice()))")
    @Mapping(source = "stockQuantity", target = "stockQuantity")
    @Mapping(source = "brand", target = "brand")
    @Mapping(source = "active", target = "active")
    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(source = "updatedAt", target = "updatedAt")
    Product toEntity(ProductDocument document);

    // Helper methods for complex mappings
    default BigDecimal mapPriceToBigDecimal(Price price) {
        return price != null ? price.getAmount() : null;
    }

    default String getCategoryNameFromId(Long categoryId) {
        // You can implement this to fetch category name from database
        // For now, return a default value
        return categoryId != null ? "Category " + categoryId : "Unknown";
    }

    default List<String> generateTagsFromProduct(Product product) {
        // Generate tags based on product attributes
        List<String> tags = new java.util.ArrayList<>();

        if (product.getName() != null) {
            // Add name-based tags
            String[] nameWords = product.getName().toLowerCase().split("\\s+");
            tags.addAll(Arrays.asList(nameWords));
        }

        if (product.getBrand() != null) {
            tags.add(product.getBrand().toLowerCase());
        }

        if (product.getCategoryId() != null) {
            tags.add("category_" + product.getCategoryId());
        }

        // Add product type tags
        tags.add("product");
        if (product.isActive()) {
            tags.add("active");
        }

        return tags;
    }

    default Price createPriceFromAmount(BigDecimal amount) {
        // Create Price value object from BigDecimal
        if (amount != null) {
            return new Price(amount, "USD");
        }
        return null;
    }
}
