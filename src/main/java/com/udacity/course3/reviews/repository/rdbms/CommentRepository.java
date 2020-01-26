package com.udacity.course3.reviews.repository.rdbms;

import com.udacity.course3.reviews.entity.rdbms.Comment;
import com.udacity.course3.reviews.entity.rdbms.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findAllByReview(Review review);
}
