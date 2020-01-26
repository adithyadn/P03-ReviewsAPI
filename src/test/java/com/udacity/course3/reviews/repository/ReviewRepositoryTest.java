package com.udacity.course3.reviews.repository;

import com.udacity.course3.reviews.entity.mongodb.Review;
import com.udacity.course3.reviews.repository.mongodb.ReviewMongoRepository;
import com.udacity.course3.reviews.repository.rdbms.ReviewRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;

import static com.udacity.course3.reviews.util.ReviewApplicationUtil.getMockMongoComment;
import static com.udacity.course3.reviews.util.ReviewApplicationUtil.getMockMongoReview;
import static org.junit.Assert.assertEquals;


@RunWith(SpringRunner.class)
@DataMongoTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class ReviewRepositoryTest {

    @Autowired
    private ReviewMongoRepository reviewRepository;

    @Test
    public void testMongoDBIntegration() {
        reviewRepository.save(getMockMongoReview());

        assertEquals(reviewRepository.findAllByProductId(999).size(), 1);
        assertEquals(reviewRepository.findAllByProductId(999).get(0).getTitle(), "Mock Review");
    }

    @Test
    public void testMongoDBIntegrationAddComments() {
        Review mockReview = getMockMongoReview();
        mockReview.setComments(Collections.singletonList(getMockMongoComment()));

        reviewRepository.save(mockReview);

        assertEquals(reviewRepository.findAllByProductId(999).size(), 1);
        assertEquals(reviewRepository.findAllByProductId(999).get(0).getTitle(), "Mock Review");
        assertEquals(reviewRepository.findAllByProductId(999).get(0).getComments().size(), 1);
        assertEquals(reviewRepository.findAllByProductId(999).get(0).getComments().get(0).getCommentText(), "Mock Comment");

    }


}
