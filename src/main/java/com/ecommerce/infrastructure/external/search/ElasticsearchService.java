package com.ecommerce.infrastructure.external.search;

import com.ecommerce.adapter.persistence.elasticsearch.repository.ProductSearchRepository;
import com.ecommerce.adapter.persistence.elasticsearch.mapper.ProductDocumentMapper;
import com.ecommerce.adapter.persistence.elasticsearch.document.ProductDocument;
import com.ecommerce.core.domain.product.entity.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(name = "spring.elasticsearch.enabled", havingValue = "true", matchIfMissing = false)
public class ElasticsearchService {

    private final ProductSearchRepository productSearchRepository;
    private final ProductDocumentMapper productDocumentMapper;

    public void indexProduct(Product product) {
        ProductDocument document = productDocumentMapper.toDocument(product);
        productSearchRepository.save(document);
    }

    public void deleteProduct(Long productId) {
        productSearchRepository.deleteById(String.valueOf(productId));
    }
}
