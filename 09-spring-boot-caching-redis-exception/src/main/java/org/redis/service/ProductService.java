package org.redis.service;

import org.redis.dto.request.ProductRequest;
import org.redis.dto.response.ProductResponse;
import org.redis.entity.ProductEntity;
import org.redis.exception.InValidDetailsEcception;
import org.redis.repository.ProductRepos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class ProductService {
	
	
	
	@Autowired
	ProductRepos repos;

	
	@Transactional
	public ProductResponse addProduct(ProductRequest request) {
		
		ProductResponse response=null;
		
		
		if(request.getProductName()==null || request.getProductName().isBlank()) {
			System.out.println("Enter valid name of product");
			throw new InValidDetailsEcception("Enter valid name of product");
		}
		
		if(request.getAmount() == null || request.getAmount() <= 0) {
			System.out.println("Give valid amount and amount should be greater than 0");
			throw new InValidDetailsEcception("Give valid amount and amount should be greater than 0");
		}
		
		
		ProductEntity productEntity=new ProductEntity();
		
		productEntity.setProductName(request.getProductName());
		productEntity.setDescription(request.getDescription());
		productEntity.setBrand(request.getBrand());
		productEntity.setAmount(request.getAmount());
		productEntity.setQty(request.getQty());
		productEntity.setMessage(generateProductMessage(productEntity));
		
		ProductEntity savedEntity=repos.save(productEntity);
		
		if(savedEntity.getProductId()>0) {
			System.out.println("Product saved into db.....");
			response=new ProductResponse();
			response.setProductName(savedEntity.getProductName());
			response.setAmount(savedEntity.getAmount());
			response.setMessage(savedEntity.getMessage());
		}
		
		
		return response;
	}
	
	

	@Cacheable(value="product_tb_redis", key="#productId" )
	public ProductEntity findByProductId(int productId) {
		
		System.out.println("Getting data from database without cashing..........");
		
		return repos.findByProductId(productId);
	}
	
	private static String generateProductMessage(ProductEntity entity) {
	    String stockNote = entity.getQty() < 10
	        ? "Note: stock is low, consider adding more units soon."
	        : "Stock levels look good.";

	    return String.format(
	        "Hi Seller, your product '%s' (Brand: %s) is being listed on the panel. " +
	        "Price: ₹%.2f, Quantity: %d. %s",
	        entity.getProductName(),
	        entity.getBrand(),
	        entity.getAmount(),
	        entity.getQty(),
	        stockNote
	    );
	}

}
