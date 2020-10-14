package com.example.service;

import com.example.dto.ProductDto;
import com.example.dto.ProductPostDto;

import java.util.List;
import java.util.Optional;

public interface ProductsService {

    List<ProductDto> findAll();

    Optional<ProductDto> findById(Long productId);

    ProductDto save(ProductPostDto postDto);

    ProductDto update(Long productId, ProductPostDto postDto);

    void delete(Long productId);
}
