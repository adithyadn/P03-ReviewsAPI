package com.udacity.course3.reviews.repository;

import com.udacity.course3.reviews.entity.rdbms.Comment;
import com.udacity.course3.reviews.entity.rdbms.Product;
import com.udacity.course3.reviews.entity.rdbms.Review;
import com.udacity.course3.reviews.repository.rdbms.CommentRepository;
import com.udacity.course3.reviews.repository.rdbms.ProductRepository;
import com.udacity.course3.reviews.repository.rdbms.ReviewRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.junit4.SpringRunner;

import static com.udacity.course3.reviews.util.ReviewApplicationUtil.*;
import static junit.framework.TestCase.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void testH2Integration() {

        Product product = getMockProduct();
        product.setProductID(1);
        Comment comment = getMockComment();
        comment.setCommentID(1);
        Review review = getMockReview();
        review.setReviewID(1);
        review.getProduct().setProductID(1);

        comment.setReview(review);
        productRepository.save(product);
        reviewRepository.save(review);
        commentRepository.save(comment);


        assertEquals(commentRepository.findAllByReview(review).size(), 1);

        assertEquals(commentRepository.findAllByReview(review).get(0).getCommentText(), "Mock Comment");
    }
}
