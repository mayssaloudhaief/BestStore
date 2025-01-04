package com.storeTool.BestStore.dao;

import com.storeTool.BestStore.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 */
@Repository
public interface ProductsRepository extends JpaRepository<Product, Long> {
}
