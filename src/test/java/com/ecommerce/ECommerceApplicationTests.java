package com.ecommerce;

import com.ecommerce.adapter.persistence.elasticsearch.repository.ProductSearchRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class ECommerceApplicationTests {

    @MockBean
    private ProductSearchRepository productSearchRepository;

    @Test
    void contextLoads() {
    }

}
