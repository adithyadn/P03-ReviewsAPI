package com.udacity.course3.reviews.controller;

import com.udacity.course3.reviews.entity.mongodb.Comment;
import com.udacity.course3.reviews.entity.mongodb.Review;
import com.udacity.course3.reviews.repository.mongodb.ReviewMongoRepository;
import com.udacity.course3.reviews.repository.rdbms.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;

/**
 * Spring REST controller for working with comment entity.
 */
@RestController
@RequestMapping("/comments")
public class CommentsController {

    // TODO: Wire needed JPA repositories here
    @Autowired
    private ReviewMongoRepository reviewMongoRepository;

    @Autowired
    private CommentRepository commentRepository;

    /**
     * Creates a comment for a review.
     *
     * 1. Add argument for comment entity. Use {@link RequestBody} annotation.
     * 2. Check for existence of review.
     * 3. If review not found, return NOT_FOUND.
     * 4. If found, save comment.
     *
     * @param reviewId The id of the review.
     */
    @RequestMapping(value = "/reviews/{reviewId}", method = RequestMethod.POST)
    public ResponseEntity<Comment> createCommentForReview(@PathVariable("reviewId") Integer reviewId
            , @RequestBody com.udacity.course3.reviews.entity.rdbms.Comment comment) {

        Review mongoReview = reviewMongoRepository.findById(reviewId)
                .orElseThrow( () ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Review not found in the repository"));

        com.udacity.course3.reviews.entity.rdbms.Review review = new com.udacity.course3.reviews.entity.rdbms.Review();
        review.setTitle(mongoReview.getTitle());
        review.setDescription(mongoReview.getDescription());
        review.setReviewID(mongoReview.getReviewID());
        comment.setReview(review);

        comment = commentRepository.save(comment);

        Comment mongoComment = new Comment();
        mongoComment.setCommentID(comment.getCommentID());
        mongoComment.setCommentText(comment.getCommentText());
        if(mongoReview.getComments() == null) {
            mongoReview.setComments(Collections.singletonList(mongoComment));
        } else {
            mongoReview.getComments().add(mongoComment);
        }

        mongoReview = reviewMongoRepository.save(mongoReview);
        return ResponseEntity.ok(mongoComment);
    }

    /**
     * List comments for a review.
     *
     * 2. Check for existence of review.
     * 3. If review not found, return NOT_FOUND.
     * 4. If found, return list of comments.
     *
     * @param reviewId The id of the review.
     */
    @RequestMapping(value = "/reviews/{reviewId}", method = RequestMethod.GET)
    public List<Comment> listCommentsForReview(@PathVariable("reviewId") Integer reviewId) {
        Review review = reviewMongoRepository.findById(reviewId)
                .orElseThrow( () ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Review not found in the repository"));

        return review.getComments();

    }
}