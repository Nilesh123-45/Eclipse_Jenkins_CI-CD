package org.catalog.repos;

import org.catalog.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepos extends MongoRepository<ProductEntity, String> {

    Page<ProductEntity> findByCategoryName(String categoryName, Pageable pageable);

    ProductEntity findByProductName(String productName);

    Page<ProductEntity> findAll(Pageable pageable);
}