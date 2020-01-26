package com.udacity.course3.reviews.repository.rdbms;

import com.udacity.course3.reviews.entity.rdbms.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
}
