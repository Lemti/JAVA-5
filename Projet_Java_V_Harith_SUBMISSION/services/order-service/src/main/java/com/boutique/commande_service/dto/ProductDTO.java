package com.boutique.commande_service.dto;

public record ProductDTO(
    Long id,
    String name,
    String description,
    Double price,
    Integer stockQuantity
) {}
