package org.catalog.controller;

import org.catalog.dto.request.ProductRequest;
import org.catalog.dto.response.ProductResponse;
import org.catalog.service.ProductService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/uploadProducts")
    public ProductResponse addProduct(@RequestBody ProductRequest request) {

//    public ResponseEntity<ProductResponse> addProduct(@RequestBody ProductRequest request) {
        ProductResponse response = productService.addProductToCategory(request);
        return response;
        //return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/category/{categoryName}")
    public List<ProductResponse> getProductsByCategory(
//    public ResponseEntity<List<ProductResponse>> getProductsByCategory(
            @PathVariable String categoryName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        List<ProductResponse> response = productService.getProductsByCategory(categoryName, page, size);
        return response;
       // return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/product/{productName}")
    public ResponseEntity<ProductResponse> getProductByName(@PathVariable String productName) {
        ProductResponse response = productService.getProductByName(productName);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/getAll")
//    public ResponseEntity<List<ProductResponse>> getAllProducts(
    public List<ProductResponse> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

//        List<ProductResponse> response = productService.getAllProducts(page, size);
//        return new ResponseEntity<>(response, HttpStatus.OK);
    	
        return productService.getAllProducts(page, size);

    }
}