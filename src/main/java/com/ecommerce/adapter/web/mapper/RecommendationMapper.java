package com.ecommerce.adapter.web.mapper;

import com.ecommerce.adapter.web.dto.response.RecommendationDto;
import com.ecommerce.core.domain.recommendation.entity.Recommendation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = ProductMapper.class)
public interface RecommendationMapper {

    RecommendationMapper INSTANCE = Mappers.getMapper(RecommendationMapper.class);

    @Mapping(target = "product", source = "product")
    RecommendationDto toDto(Recommendation recommendation);
}