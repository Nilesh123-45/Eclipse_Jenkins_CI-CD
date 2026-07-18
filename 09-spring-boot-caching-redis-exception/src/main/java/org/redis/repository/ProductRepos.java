package org.redis.repository;

import org.redis.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepos extends JpaRepository<ProductEntity, Integer>{

	public abstract ProductEntity findByProductId(int productId);
}
