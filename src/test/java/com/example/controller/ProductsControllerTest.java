package com.example.controller;

import com.example.dto.ProductDto;
import com.example.dto.ProductPostDto;
import com.example.service.ProductsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.Assert;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ProductsControllerTest {

    @Mock
    private ProductsService testRunService;

    @InjectMocks
    private ProductsController testee;

    private static final String controllerUrl = "/test/products";

    private WebTestClient testClient;

    int repositoryTestSize;
    ArrayList<ProductDto> products;

    @BeforeEach
    private void initMocks() {
        products = new ArrayList<>();
        products.add(new ProductDto(1L, "1-prod", 101, Instant.now()));
        products.add(new ProductDto(2L, "2-prod", 102, Instant.now()));
        products.add(new ProductDto(3L, "3-prod", 103, Instant.now()));
        products.add(new ProductDto(4L, "4-prod", 104, Instant.now()));
        products.add(new ProductDto(5L, "5-prod", 105, Instant.now()));
        repositoryTestSize = products.size();

        when(testRunService.findAll()).thenReturn(products);

        when(testRunService.findById(anyLong())).thenAnswer((
                InvocationOnMock invocation) -> {
            int i = ((Long) invocation.getArguments()[0]).intValue();
            if (i > repositoryTestSize) {
                return Optional.empty();
            } else {
                return Optional.of(products.get(i));
            }
        });

        when(testRunService.update(anyLong(), any())).thenAnswer((
                InvocationOnMock invocation) -> {
            int i = ((Long) invocation.getArguments()[0]).intValue();
            if (i > repositoryTestSize) {
                throw new IllegalArgumentException("NotFound");
            } else {
                return products.get(i);
            }
        });

        testClient = WebTestClient
                .bindToController(testee)
                .build();
    }

    @Test
    void getProducts() {
        WebTestClient.ResponseSpec responseSpec = testClient.get()
                .uri(controllerUrl)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-Type", "application/json");

        List<ProductDto> responseBody = responseSpec
                .expectBody(new ParameterizedTypeReference<List<ProductDto>>() {
                })
                .returnResult()
                .getResponseBody();

        verify(testRunService, times(1)).findAll();

        Assert.notNull(responseBody, "List<ProductDto> expected, got null");

        Assert.isTrue(repositoryTestSize == responseBody.size(),
                "Expected to get the same number of products as repository returned");
    }

    @Test
    void getProductOk() {
        long productId = 4;
        WebTestClient.ResponseSpec responseSpec = testClient.get()
                .uri(controllerUrl + "/" + productId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-Type", "application/json");

        ProductDto responseBody = responseSpec
                .expectBody(new ParameterizedTypeReference<ProductDto>() {
                })
                .returnResult()
                .getResponseBody();

        verify(testRunService, times(1)).findById(productId);

        Assert.notNull(responseBody, "ProductDto expected, got null");
    }

    @Test
    void getProductNotFound() {
        long productId = repositoryTestSize + 1;
        testClient.get()
                .uri(controllerUrl + "/" + productId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound();

        verify(testRunService, times(1)).findById(productId);
    }

    @Test
    void postProductOk() {
        ProductPostDto productPostDto = new ProductPostDto("testPostProduct", 951);

        testClient.post()
                .uri(controllerUrl)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(productPostDto), ProductPostDto.class)
                .exchange()
                .expectStatus().isCreated();

        verify(testRunService, times(1)).save(any());
    }

    @Test
    void putProductOk() {
        long productId = 1;
        ProductPostDto productPostDto = new ProductPostDto("testPutProductOk", 654);

        testClient.put()
                .uri(controllerUrl + "/" + productId)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(productPostDto), ProductPostDto.class)
                .exchange()
                .expectStatus().isAccepted();

        verify(testRunService, times(1)).update(productId, productPostDto);
    }

    @Test
    void deleteProductOk() {
        long productId = 1;

        testClient.delete()
                .uri(controllerUrl + "/" + productId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk();

        verify(testRunService, times(1)).delete(productId);
    }
}