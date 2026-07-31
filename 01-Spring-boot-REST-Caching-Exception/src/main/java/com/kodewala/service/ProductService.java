package com.kodewala.service;

import com.kodewala.exception.GlobalException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kodewala.dto.request.ProductRequest;
import com.kodewala.dto.response.ProductResponse;
import com.kodewala.entity.CategoryEntity;
import com.kodewala.entity.ProductEntity;
import com.kodewala.exception.InValidProductCatalog;
import com.kodewala.repository.CategoryRepo;

@Service
public class ProductService {
	
	@Autowired
	CategoryRepo categoryRepo;


	public ProductResponse addProduct(ProductRequest request) {
		
		ProductEntity productEntity=new ProductEntity();
		CategoryEntity categoryEntity=null;
		
		ProductResponse response=new ProductResponse();
		
		if(request.getProductName()==null || request.getProductName().isBlank()) {
			System.out.println("Product name  can not be empty ");
			throw new InValidProductCatalog("Product name can not be empty ");
		}
		
		if(request.getCategory()==null || request.getCategory().isBlank()) {
			System.out.println("Category name  can not be empty ");
			throw new InValidProductCatalog("Category name can not be empty ");
		}
		
		
		if(request.getAmount()<=0) {
			System.out.println("Enter valid amount......");
			throw new InValidProductCatalog("Enter some valid amount.......");
		}
		
		if(request.getQty()<=0) {
			System.out.println("Enter some valid quantity......");
			throw new InValidProductCatalog("Enter some valid product quantity......");
		}
		
		categoryEntity=categoryRepo.findByCategory(request.getCategory());
		if(categoryEntity.getCategory()==null) {
			categoryEntity=new CategoryEntity();
			categoryEntity.setCategory(request.getCategory());
			categoryEntity.setStatus("ACTIVE");
		}
		
		productEntity.setProdName(request.getProductName());
		productEntity.setAmount(request.getAmount());
		productEntity.setQty(request.getQty());
		productEntity.setDescription(request.getDescription());
		productEntity.setCategory(categoryEntity);
		
		categoryEntity.getProduct().add(productEntity);
		
		CategoryEntity savedEntity=	categoryRepo.save(categoryEntity);
		response.setProductName(savedEntity.toString());
		response.setCategory(savedEntity.getCategory());
		response.setStatus(savedEntity.getStatus());
				
				return response;
	}

}
