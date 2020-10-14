package com.example.service.impl;

import com.example.dto.ProductDto;
import com.example.dto.ProductPostDto;
import com.example.model.Product;
import com.example.repository.ProductsRepository;
import com.example.service.ProductsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.RestExceptionHandler.notFound;

@Service
public class ProductsServiceImpl implements ProductsService {

    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());

    private final ProductsRepository productsRepository;

    @Autowired
    public ProductsServiceImpl(ProductsRepository productsRepository) {
        this.productsRepository = productsRepository;
    }

    @Override
    public List<ProductDto> findAll() {
        List<ProductDto> result = productsRepository.findAll().stream()
                .map(ProductsServiceImpl::entity2Dto)
                .collect(Collectors.toList());
        log.debug("findAll:{}", result.size());
        return result;
    }

    @Override
    public Optional<ProductDto> findById(Long productId) {
        Optional<ProductDto> result;
        Optional<Product> byId = productsRepository.findById(productId);
        result = byId.map(ProductsServiceImpl::entity2Dto);
        log.debug("findById:{}", result);
        return result;
    }

    @Override
    public ProductDto save(ProductPostDto postDto) {
        ProductDto result;
        Product saved = productsRepository.save(dto2Entity(postDto));
        result = entity2Dto(saved);
        log.debug("save:{}", result);
        return result;
    }

    @Override
    public ProductDto update(Long productId, ProductPostDto postDto) {
        if (findById(productId).isPresent()) {
            ProductDto result;
            Product product = dto2Entity(postDto);
            product.setId(productId);
            result = entity2Dto(productsRepository.save(product));
            log.debug("update:{}", result);
            return result;
        } else {
            throw new IllegalArgumentException("Product with Id:" + productId + notFound);
        }
    }

    @Override
    public void delete(Long productId) {
        productsRepository.deleteById(productId);
    }

    public static ProductDto entity2Dto(Product entity) {
        return new ProductDto(entity.getId(), entity.getName(), entity.getPrice(), entity.getDate());
    }

    public static Product dto2Entity(ProductPostDto postDto) {
        return new Product(null, postDto.getName(), postDto.getPrice(), null);
    }
}
