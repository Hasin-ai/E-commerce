package com.ecommerce.adapter.web.controller;

import com.ecommerce.adapter.web.dto.request.search.SearchRequestDto;
import com.ecommerce.adapter.web.dto.response.search.SearchResultDto;
import com.ecommerce.adapter.web.mapper.SearchMapper;
import com.ecommerce.core.domain.search.entity.SearchResult;
import com.ecommerce.core.usecase.search.SearchProductUseCase;
import com.ecommerce.shared.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ecommerce.infrastructure.security.CustomUserDetails;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchProductUseCase searchProductUseCase;
    private final SearchMapper searchMapper;

    @PostMapping
    public ResponseEntity<ApiResponse<SearchResultDto>> searchProducts(
            @Valid @RequestBody SearchRequestDto searchRequestDto,
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        String userId = (currentUser != null) ? currentUser.getId().toString() : null;
        SearchResult searchResult = searchProductUseCase.execute(
            searchRequestDto.getQuery(), 
            userId,
            searchRequestDto.getPage(),
            searchRequestDto.getPageSize()
        );
        SearchResultDto searchResultDto = searchMapper.toDto(searchResult);
        return ResponseEntity.ok(new ApiResponse<>(true, searchResultDto, "Products found"));
    }
}
