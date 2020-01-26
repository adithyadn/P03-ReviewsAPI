package com.udacity.course3.reviews.controller;

import com.udacity.course3.reviews.entity.rdbms.Review;
import com.udacity.course3.reviews.repository.mongodb.ReviewMongoRepository;
import com.udacity.course3.reviews.repository.rdbms.ProductRepository;
import com.udacity.course3.reviews.repository.rdbms.ReviewRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
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

import static com.udacity.course3.reviews.util.ReviewApplicationUtil.*;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private ReviewRepository reviewRepository;

    @MockBean
    private ReviewMongoRepository reviewMongoRepository;

    @Autowired
    private JacksonTester<Review> json;

    @Test
    public void testCreateReview() throws Exception {

        BDDMockito.given(productRepository.findById(any())).willReturn(Optional.of(getMockProduct()));
        BDDMockito.given(reviewRepository.save(any())).willReturn(getMockReview());
        BDDMockito.given(reviewMongoRepository.save(any())).willReturn(getMockMongoReview());

        mockMvc.perform(MockMvcRequestBuilders.post("/reviews/products/999")
                .content(json.write(getMockReview()).getJson())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Mock Review"))
        ;
    }

    @Test
    public void testCreateReviewWhenNoProductAvailable() throws Exception {
        BDDMockito.given(productRepository.findById(999)).willReturn(Optional.of(getMockProduct()));
        BDDMockito.given(reviewRepository.save(any())).willReturn(getMockReview());

        mockMvc.perform(MockMvcRequestBuilders.post("/reviews/products/123")
                .content(json.write(getMockReview()).getJson())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNotFound())
        ;
    }

    @Test
    public void testFetchAllReviewsByProductId() throws Exception {
        BDDMockito.given(productRepository.findById(999)).willReturn(Optional.of(getMockProduct()));
        Review mockReview = getMockReview();
        mockReview.setProduct(getMockProduct());
        BDDMockito.given(reviewRepository.findAllByProduct(any()))
                .willReturn(Collections.singletonList(mockReview));

        BDDMockito.given(reviewMongoRepository.findAllByProductId(any()))
                .willReturn(Collections.singletonList(getMockMongoReview()));

        mockMvc.perform(MockMvcRequestBuilders.get("/reviews/products/999"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value("Mock Review"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].productId").value(999));
    }

    @Test
    public void testFetchAllReviewsByInvalidProductId() throws Exception {
        BDDMockito.given(productRepository.findById(999)).willReturn(Optional.of(getMockProduct()));
        Review mockReview = getMockReview();
        mockReview.setProduct(getMockProduct());
        BDDMockito.given(reviewRepository.findAllByProduct(any()))
                .willReturn(Collections.singletonList(mockReview));

        mockMvc.perform(MockMvcRequestBuilders.get("/reviews/products/123"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                ;
    }



}
