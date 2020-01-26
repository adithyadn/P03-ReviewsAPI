package com.udacity.course3.reviews.controller;

import com.udacity.course3.reviews.entity.mongodb.Review;
import com.udacity.course3.reviews.entity.rdbms.Product;
import com.udacity.course3.reviews.repository.mongodb.ReviewMongoRepository;
import com.udacity.course3.reviews.repository.rdbms.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.Transient;
import java.util.List;

/**
 * Spring REST controller for working with review entity.
 */
@RestController
public class ReviewsController {

    // TODO: Wire JPA repositories here

    @Autowired
    private com.udacity.course3.reviews.repository.rdbms.ReviewRepository reviewRepository;
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ReviewMongoRepository reviewMongoRepository;
    /**
     * Creates a review for a product.
     * <p>
     * 1. Add argument for review entity. Use {@link RequestBody} annotation.
     * 2. Check for existence of product.
     * 3. If product not found, return NOT_FOUND.
     * 4. If found, save review.
     *
     * @param productId The id of the product.
     * @return The created review or 404 if product id is not found.
     */
    @RequestMapping(value = "/reviews/products/{productId}", method = RequestMethod.POST)
    public ResponseEntity<Review> createReviewForProduct(@PathVariable("productId") Integer productId
            , @RequestBody com.udacity.course3.reviews.entity.rdbms.Review review) {

        Product product = productRepository.findById(productId)
                .orElseThrow( () ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Product ID not found in the repository"));

        review.setProduct(product);
        review = reviewRepository.save(review);

        Review mongoReview = new Review();
        mongoReview.setDescription(review.getDescription());
        mongoReview.setDownVote(review.getDownVote());
        mongoReview.setUpVote(review.getUpVote());
        mongoReview.setTitle(review.getTitle());
        mongoReview.setReviewID(review.getReviewID());
        mongoReview.setProductId(review.getProduct().getProductID());

        return ResponseEntity.ok(reviewMongoRepository.save(mongoReview));

    }

    /**
     * Lists reviews by product.
     *
     * @param productId The id of the product.
     * @return The list of reviews.
     */
    @RequestMapping(value = "/reviews/products/{productId}", method = RequestMethod.GET)
    public ResponseEntity<List<Review>> listReviewsForProduct(@PathVariable("productId") Integer productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow( () ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Product ID not found in the repository"));

        //List<com.udacity.course3.reviews.entity.rdbms.Review> reviews = reviewRepository.findAllByProduct(product);
        List<Review> mongoReviews = reviewMongoRepository.findAllByProductId(productId);

        return ResponseEntity.ok(mongoReviews);
    }
}