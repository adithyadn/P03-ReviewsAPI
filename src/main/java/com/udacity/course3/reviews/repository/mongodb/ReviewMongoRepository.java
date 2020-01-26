package com.udacity.course3.reviews.repository.mongodb;

import com.udacity.course3.reviews.entity.mongodb.Review;
import com.udacity.course3.reviews.entity.rdbms.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewMongoRepository extends MongoRepository<Review, Integer> {
    List<Review> findAllByProductId(Integer productId);
    Optional<Review> findByReviewID(Integer reviewID);
}
