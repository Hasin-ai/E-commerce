package com.ecommerce.adapter.web.mapper;

import com.ecommerce.adapter.web.dto.response.ProductResponseDto;
import com.ecommerce.adapter.web.dto.response.search.SearchResultDto;
import com.ecommerce.core.domain.product.entity.Product;
import com.ecommerce.core.domain.search.entity.SearchResult;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = ProductMapper.class)
public interface SearchMapper {

    SearchMapper INSTANCE = Mappers.getMapper(SearchMapper.class);

    @Mapping(target = "products", source = "products")
    SearchResultDto toDto(SearchResult searchResult);

    default List<ProductResponseDto> mapProducts(List<Product> products) {
        ProductMapper productMapper = new ProductMapper();
        return products.stream()
                .map(productMapper::toProductResponse)
                .collect(Collectors.toList());
    }
}