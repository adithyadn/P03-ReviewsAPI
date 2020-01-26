package com.udacity.course3.reviews.controller;

import com.udacity.course3.reviews.entity.rdbms.Product;
import com.udacity.course3.reviews.repository.rdbms.ProductRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;
import java.util.Optional;

import static com.udacity.course3.reviews.util.ReviewApplicationUtil.getMockProduct;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductRepository productRepository;

    @Autowired
    private JacksonTester<Product> json;

    @Test
    public void testListAllProductsWhenNoData() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/products/"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
        ;
    }

    @Test
    public void testMockListAllProducts() throws Exception {
        BDDMockito.given(productRepository.findAll()).willReturn(Collections.singletonList(getMockProduct()));
        mockMvc.perform(MockMvcRequestBuilders.get("/products/"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].productID").value(Integer.valueOf(999)));
    }

    @Test
    public void testCreateProduct() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/products/")
                .content(json.write(getMockProduct()).getJson())
                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(MockMvcResultMatchers.status().isCreated())
                ;
    }


    @Test
    public void testMockFindProductById() throws Exception {
        BDDMockito.given(productRepository.findById(any())).willReturn(Optional.of(getMockProduct()));
        mockMvc.perform(MockMvcRequestBuilders.get("/products/999"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.productID").value(Integer.valueOf(999)));
    }


    @Test
    public void testInvalidProductGet() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/products/1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

}
