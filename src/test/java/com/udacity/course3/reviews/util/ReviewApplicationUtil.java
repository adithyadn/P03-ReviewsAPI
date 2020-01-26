package com.udacity.course3.reviews.util;

import com.udacity.course3.reviews.entity.rdbms.Comment;
import com.udacity.course3.reviews.entity.rdbms.Product;
import com.udacity.course3.reviews.entity.rdbms.Review;

public class ReviewApplicationUtil {
    public static Product getMockProduct() {
        Product product = new Product();
        product.setProductID(999);
        product.setDescription("Plays Videos");
        product.setProductName("YouTube");

        return product;
    }

    public static Review getMockReview() {
        Review review = new Review();
        review.setTitle("Mock Review");
        review.setDescription("Mock Review using Mockito");
        review.setDownVote(0);
        review.setUpVote(0);
        review.setReviewID(888);
        review.setProduct(getMockProduct());

        return review;
    }

    public static com.udacity.course3.reviews.entity.mongodb.Review getMockMongoReview() {
        com.udacity.course3.reviews.entity.mongodb.Review review = new com.udacity.course3.reviews.entity.mongodb.Review();
        review.setTitle("Mock Review");
        review.setDescription("Mock Review using Mockito");
        review.setDownVote(0);
        review.setUpVote(0);
        review.setReviewID(888);
        review.setProductId(999);

        return review;
    }

    public static Comment getMockComment() {
        Comment comment = new Comment();
        comment.setCommentID(777);
        comment.setCommentText("Mock Comment");

        return comment;
    }

    public static com.udacity.course3.reviews.entity.mongodb.Comment getMockMongoComment() {
        com.udacity.course3.reviews.entity.mongodb.Comment comment = new com.udacity.course3.reviews.entity.mongodb.Comment();
        comment.setCommentID(777);
        comment.setCommentText("Mock Comment");

        return comment;
    }
}
