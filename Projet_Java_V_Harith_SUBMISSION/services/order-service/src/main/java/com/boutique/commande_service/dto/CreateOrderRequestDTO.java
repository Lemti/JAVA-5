package com.boutique.commande_service.dto;

import java.util.List;

public record CreateOrderRequestDTO(
    List<OrderLineItemDTO> orderLines
) {}
