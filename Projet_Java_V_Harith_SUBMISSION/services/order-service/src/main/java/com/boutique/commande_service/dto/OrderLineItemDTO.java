package com.boutique.commande_service.dto;

public record OrderLineItemDTO(
    Long productId,
    Integer quantity
) {}
