package org.redis.controller;

import org.redis.dto.request.ProductRequest;
import org.redis.dto.response.ProductResponse;
import org.redis.entity.ProductEntity;
import org.redis.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1.0/zepto/")
public class ProductController {
	
	@Autowired
	ProductService service;
	
	
	@PostMapping("upload")
	public ProductResponse uploadProduct(@RequestBody ProductRequest request) {
		
		return service.addProduct(request);
	}
	
	@GetMapping("findById/{productId}")
	public ProductEntity getProductById(@PathVariable int productId) {
		return service.findByProductId(productId);
	}

}
