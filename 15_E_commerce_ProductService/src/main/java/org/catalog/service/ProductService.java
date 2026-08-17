package org.catalog.service;

import java.util.List;
import java.util.stream.Collectors;

import org.catalog.dto.request.ProductRequest;
import org.catalog.dto.response.ProductResponse;
import org.catalog.entity.ProductEntity;
import org.catalog.exception.InValidDetailsException;
import org.catalog.repos.ProductRepos;

import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;

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

    @Transactional
    @CacheEvict(value = "category_cache", key = "#request.categoryName")
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

        kafkaTemplate.send(MANAGE_PRODUCT_TOPIC, saved.getProductId(), message);
        log.info("Kafka message sent to topic '{}': {}", MANAGE_PRODUCT_TOPIC, message);

        return toResponse(saved);
    }

    @Transactional
    public List<ProductResponse> getAllProducts(int pageNumber, int pageSize) {

        Pageable pageRequest = PageRequest.of(pageNumber, pageSize);
        Page<ProductEntity> page = productRepos.findAll(pageRequest);

        return page.getContent().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    @Cacheable(value = "category_cache", key = "#categoryName")
    public List<ProductResponse> getProductsByCategory(String categoryName, int pageNumber, int pageSize) {

        Pageable pageRequest = PageRequest.of(pageNumber, pageSize);
        Page<ProductEntity> page = productRepos.findByCategoryName(categoryName, pageRequest);

        return page.getContent().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // Get a single product by its exact name
    @Transactional
    public ProductResponse getProductByName(String productName) {

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