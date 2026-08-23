package org.catalog.service;

import java.util.ArrayList;
import java.util.List;

import org.catalog.dto.request.ProductRequest;
import org.catalog.dto.response.ProductResponse;
import org.catalog.entity.ProductEntity;
import org.catalog.exception.InValidDetailsException;
import org.catalog.repos.ProductRepos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;

@Service
public class ProductService {

    private static final Logger log = LoggerFactory.getLogger(ProductService.class);

    private static final String MANAGE_PRODUCT_TOPIC = "manage_product";

    @Autowired
    private ProductRepos productRepos;

    @Autowired
    private Tracer tracer;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    // adding a product touches all three read caches, so wipe all of them here
    @Transactional
    @CacheEvict(value = { "category_cache", "all_products_cache" }, allEntries = true)
    public ProductResponse addProductToCategory(ProductRequest request) {

        Span currentSpan = tracer.currentSpan();

        if (request.getCategoryName() == null || request.getCategoryName().isBlank()
                || request.getProductName() == null || request.getProductName().isBlank()) {
            throw new InValidDetailsException("Category Name || Product Name can not be null.....");
        }

        if (request.getQty() <= 0) {
            throw new InValidDetailsException("Quantity can not be negative.......");
        }

        if (request.getAmount() == null || request.getAmount() <= 100) {
            throw new InValidDetailsException("please enter valid amount");
        }

        if (currentSpan != null) {
            log.info("PRODUCT TRACE -> traceId={}, spanId={}",
                    currentSpan.context().traceId(), currentSpan.context().spanId());
        } else {
            log.warn("PRODUCT TRACE -> No current span found");
        }

        ProductEntity productEntity = new ProductEntity();
        productEntity.setProductName(request.getProductName());
        productEntity.setCategoryName(request.getCategoryName());
        productEntity.setDescription(request.getDescription());
        productEntity.setQty(request.getQty());
        productEntity.setAmount(request.getAmount());
        productEntity.setProductStatus(request.getQty() > 0 ? "Active" : "Inactive - Out of Stock");

        ProductEntity saved = productRepos.save(productEntity);

        log.info("Product '{}' added to category '{}' successfully.....",
                saved.getProductName(), saved.getCategoryName());

        String message = "Product '" + saved.getProductName()
                + "' added to category '" + saved.getCategoryName()
                + "' successfully.";

        // productId used as Kafka key (guaranteed unique, unlike productName)
        kafkaTemplate.send(MANAGE_PRODUCT_TOPIC, saved.getProductId(), message);
        log.info("Kafka message sent to topic '{}': {}", MANAGE_PRODUCT_TOPIC, message);

        return toResponse(saved);
    }

    @Transactional
    @Cacheable(value = "all_products_cache", key = "#pageNumber + '_' + #pageSize")
    public List<ProductResponse> getAllProducts(int pageNumber, int pageSize) {
        
        // This log ONLY prints if the cache was missed (or evicted/expired)
        log.info("CACHE MISS: Very first time hitting the root db instead of Redis for All Products (Page: {})", pageNumber);

        Pageable pageRequest = PageRequest.of(pageNumber, pageSize);
        Page<ProductEntity> page = productRepos.findAll(pageRequest);

        // Standard Loop Approach
        List<ProductResponse> responseList = new ArrayList<>();
        for (ProductEntity entity : page.getContent()) {
            ProductResponse response = toResponse(entity);
            responseList.add(response);
        }

        return responseList;
    }

    @Transactional
    @Cacheable(value = "category_cache", key = "#categoryName + '_' + #pageNumber + '_' + #pageSize")
    public List<ProductResponse> getProductsByCategory(String categoryName, int pageNumber, int pageSize) {
        
        // This log ONLY prints if the cache was missed
        log.info("CACHE MISS: Very first time hitting the root db instead of Redis for Category: {}", categoryName);

        Pageable pageRequest = PageRequest.of(pageNumber, pageSize);
        Page<ProductEntity> page = productRepos.findByCategoryName(categoryName, pageRequest);

        // Standard Loop Approach
        List<ProductResponse> responseList = new ArrayList<>();	
        for (ProductEntity entity : page.getContent()) {
            ProductResponse response = toResponse(entity);
            responseList.add(response);
        }

        return responseList;
    }

    // single product lookup, cached by exact productName
    @Transactional
    @Cacheable(value = "product_by_name", key = "#productName")
    public ProductResponse getProductByName(String productName) {

        // This log ONLY prints if the cache was missed
        log.info("CACHE MISS: Very first time hitting the root db instead of Redis for Product: {}", productName);
        System.out.println("Hitting the root db.....");

        ProductEntity productEntity = productRepos.findByProductName(productName);

        if (productEntity == null) {
            throw new InValidDetailsException("Product '" + productName + "' does not exist.......");
        }

        return toResponse(productEntity);
    }

    private ProductResponse toResponse(ProductEntity entity) {

        ProductResponse response = new ProductResponse();
        response.setProductId(entity.getProductId());
        response.setProductName(entity.getProductName());
        response.setCategoryName(entity.getCategoryName());
        response.setDescription(entity.getDescription());
        response.setQty(entity.getQty());
        response.setAmount(entity.getAmount());
        response.setProductStatus(entity.getProductStatus());
        return response;
    }
}