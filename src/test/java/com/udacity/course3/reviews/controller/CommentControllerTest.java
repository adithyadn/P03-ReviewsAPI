package com.udacity.course3.reviews.controller;

import com.udacity.course3.reviews.entity.mongodb.Review;
import com.udacity.course3.reviews.entity.rdbms.Comment;
import com.udacity.course3.reviews.repository.mongodb.ReviewMongoRepository;
import com.udacity.course3.reviews.repository.rdbms.CommentRepository;
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
public class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentRepository commentRepository;

    @MockBean
    private ReviewRepository reviewRepository;

    @MockBean
    private ReviewMongoRepository reviewMongoRepository;

    @Autowired
    private JacksonTester<Comment> json;

    @Test
    public void testCreateComment() throws Exception {

        BDDMockito.given(reviewRepository.findById(any())).willReturn(Optional.of(getMockReview()));
        BDDMockito.given(reviewMongoRepository.findById(any())).willReturn(Optional.of(getMockMongoReview()));
        BDDMockito.given(commentRepository.save(any())).willReturn(getMockComment());

        mockMvc.perform(MockMvcRequestBuilders.post("/comments/reviews/888")
                .content(json.write(getMockComment()).getJson())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.commentText").value("Mock Comment"))
        ;
    }

    @Test
    public void testCreateCommentWhenNoReviewAvailable() throws Exception {
        BDDMockito.given(reviewRepository.findById(888)).willReturn(Optional.of(getMockReview()));
        BDDMockito.given(commentRepository.save(any())).willReturn(getMockComment());

        mockMvc.perform(MockMvcRequestBuilders.post("/comments/reviews/123")
                .content(json.write(getMockComment()).getJson())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNotFound())
        ;
    }

    @Test
    public void testFetchAllCommentsByReviewId() throws Exception {

        Review mockMongoReview = getMockMongoReview();
        mockMongoReview.setComments(Collections.singletonList(getMockMongoComment()));
        BDDMockito.given(reviewMongoRepository.findById(888)).willReturn(Optional.of(mockMongoReview));

/*
        BDDMockito.given(commentRepository.findAllByReview(any()))
                .willReturn(Collections.singletonList(mockComment));
*/

        mockMvc.perform(MockMvcRequestBuilders.get("/comments/reviews/888"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].commentText").value("Mock Comment"))
                ;
    }

    @Test
    public void testFetchAllReviewsByInvalidProductId() throws Exception {
        BDDMockito.given(reviewRepository.findById(888)).willReturn(Optional.of(getMockReview()));
        Comment mockComment = getMockComment();
        mockComment.setReview(getMockReview());
        BDDMockito.given(commentRepository.findAllByReview(any()))
                .willReturn(Collections.singletonList(mockComment));

        mockMvc.perform(MockMvcRequestBuilders.get("/comments/reviews/123"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
        ;
    }
}
