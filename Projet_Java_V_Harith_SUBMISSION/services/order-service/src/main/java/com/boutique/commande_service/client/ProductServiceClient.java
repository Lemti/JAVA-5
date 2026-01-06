package com.boutique.commande_service.client;

import com.boutique.commande_service.dto.ProductDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProductServiceClient {

    private final RestTemplate restTemplate;

    @Value("${product.service.url}")
    private String productServiceUrl;

    public ProductDTO getProduct(Long productId) {
        String url = productServiceUrl + "/api/products/" + productId;
        return restTemplate.getForObject(url, ProductDTO.class);
    }

    public void decreaseStock(Long productId, Integer quantity) {
        String url = productServiceUrl + "/api/products/" + productId + "/decrease-stock";
        Map<String, Integer> request = Map.of("quantity", quantity);
        restTemplate.postForObject(url, request, ProductDTO.class);
    }
}
