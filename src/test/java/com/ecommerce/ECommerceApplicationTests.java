package com.ecommerce;

import com.ecommerce.adapter.persistence.elasticsearch.repository.ProductSearchRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
class ECommerceApplicationTests {

    @MockitoBean
    private ProductSearchRepository productSearchRepository;

    @Test
    void contextLoads() {
    }

}
