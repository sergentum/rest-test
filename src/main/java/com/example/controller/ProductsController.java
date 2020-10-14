package com.example.controller;

import com.example.dto.ProductDto;
import com.example.dto.ProductPostDto;
import com.example.service.ProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static com.example.MainApplication.APP_CONTEXT;

@RestController
@RequestMapping(APP_CONTEXT + "/products")
public class ProductsController {

    private final ProductsService productsService;

    @Autowired
    public ProductsController(ProductsService productsService) {
        this.productsService = productsService;
    }

    @GetMapping
    public List<ProductDto> getProducts() {
        return productsService.findAll();
    }

    @GetMapping(path = "/{productId}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable("productId") Long productId) {
        Optional<ProductDto> byId = productsService.findById(productId);
        return byId.map(productDto -> new ResponseEntity<>(productDto, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<ProductDto> postProduct(ProductPostDto productPostDto) {
        ProductDto save = productsService.save(productPostDto);
        return new ResponseEntity<>(save, HttpStatus.CREATED);
    }

    @PutMapping(path = "/{productId}")
    public ResponseEntity<ProductDto> putProduct(
            @PathVariable("productId") Long productId,
            @RequestBody ProductPostDto productPostDto
    ) {
        ProductDto save = productsService.update(productId, productPostDto);
        return new ResponseEntity<>(save, HttpStatus.ACCEPTED);
    }

    @DeleteMapping(path = "/{productId}")
    public ResponseEntity<Object> deleteProduct(@PathVariable("productId") Long productId) {
        productsService.delete(productId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
