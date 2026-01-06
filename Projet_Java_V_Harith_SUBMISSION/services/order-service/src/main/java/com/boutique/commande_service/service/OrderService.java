package com.boutique.commande_service.service;

import com.boutique.commande_service.client.ProductServiceClient;
import com.boutique.commande_service.dao.OrderDAO;
import com.boutique.commande_service.dto.CreateOrderRequestDTO;
import com.boutique.commande_service.dto.OrderLineItemDTO;
import com.boutique.commande_service.dto.ProductDTO;
import com.boutique.commande_service.entity.Order;
import com.boutique.commande_service.entity.OrderLine;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderDAO orderDAO;
    private final ProductServiceClient productServiceClient;

    public List<Order> getAllOrders() {
        return orderDAO.findAll();
    }

    public Order getOrderById(Long id) {
        return orderDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
    }

    @Transactional
    public Order createOrder(CreateOrderRequestDTO request) {
        Order order = new Order();
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("PENDING");
        order.setOrderLines(new ArrayList<>());

        BigDecimal totalAmount = BigDecimal.ZERO;

        for (OrderLineItemDTO item : request.orderLines()) {
            // Récupérer les infos du produit
            ProductDTO product = productServiceClient.getProduct(item.productId());

            // Vérifier le stock
            if (product.stockQuantity() < item.quantity()) {
                throw new RuntimeException("Insufficient stock for product: " + product.name());
            }

            // Créer la ligne de commande
            BigDecimal unitPrice = BigDecimal.valueOf(product.price());
            BigDecimal quantity = BigDecimal.valueOf(item.quantity());
            BigDecimal subtotal = unitPrice.multiply(quantity);

            OrderLine orderLine = new OrderLine();
            orderLine.setOrder(order);
            orderLine.setProductId(item.productId());
            orderLine.setProductName(product.name());
            orderLine.setQuantity(item.quantity());
            orderLine.setUnitPrice(unitPrice);
            orderLine.setSubtotal(subtotal);

            order.getOrderLines().add(orderLine);
            totalAmount = totalAmount.add(subtotal);
        }

        order.setTotalAmount(totalAmount);

        // Sauvegarder la commande
        Order savedOrder = orderDAO.save(order);

        // Diminuer le stock pour chaque produit
        for (OrderLineItemDTO item : request.orderLines()) {
            productServiceClient.decreaseStock(item.productId(), item.quantity());
        }

        // Confirmer la commande
        savedOrder.setStatus("CONFIRMED");
        return orderDAO.save(savedOrder);
    }

    public void deleteOrder(Long id) {
        orderDAO.deleteById(id);
    }
}