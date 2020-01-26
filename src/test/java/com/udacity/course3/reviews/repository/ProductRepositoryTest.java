package com.udacity.course3.reviews.repository;


import com.udacity.course3.reviews.repository.rdbms.ProductRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static com.udacity.course3.reviews.util.ReviewApplicationUtil.getMockProduct;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void testCanary() {
        assertTrue(true);
    }

    @Test
    public void testH2Integration() {

        productRepository.save(getMockProduct());

        assertEquals(productRepository.findById(1).get().getProductName(), getMockProduct().getProductName());
        assertEquals(productRepository.findAll().size(), 1);
    }

}
