package com.shopflow.domain.order.service;

import com.shopflow.domain.order.OrderEntity;
import com.shopflow.domain.order.OrderRepository;
import com.shopflow.domain.order.dto.OrderRequestDTO;
import com.shopflow.domain.order.dto.OrderResponseDTO;
import com.shopflow.domain.order.dto.PaymentRequestDTO;
import com.shopflow.domain.order.dto.ProductClientDTO;
import com.shopflow.domain.order.exception.OrderProcessingException;
import com.shopflow.domain.order.exception.ProductNotFoundException;
import io.awspring.cloud.sns.core.SnsTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository repository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private SnsTemplate snsTemplate;

    private static final String PRODUCT_API_URL = "http://localhost:8081/products/";
    private static final String SNS_TOPIC_ARN = "arn:aws:sns:us-east-1:000000000000:order-created-topic";

    public List<OrderResponseDTO> getUserOrders(UUID userId) {
        return repository.findByUserId(userId).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public OrderResponseDTO createOrder(OrderRequestDTO dto) {
        // 1. Fetch product price
        String productUrl = PRODUCT_API_URL + dto.productId();
        ProductClientDTO product;
        try {
            product = restTemplate.getForObject(productUrl, ProductClientDTO.class);
            if (product == null) {
                throw new ProductNotFoundException("Product with ID " + dto.productId() + " not found or unavailable.");
            }
        } catch (HttpClientErrorException.NotFound e) {
            throw new ProductNotFoundException("Product with ID " + dto.productId() + " not found.");
        } catch (Exception e) {
            throw new OrderProcessingException("Error fetching product data: " + e.getMessage(), e);
        }

        // 2. Create order as PENDING
        OrderEntity order = new OrderEntity(dto.userId(), dto.productId(), "PENDING");
        OrderEntity savedOrder = repository.save(order);

        // 3. Process payment via SNS Event
        PaymentRequestDTO paymentRequest = new PaymentRequestDTO(savedOrder.getId(), product.preco());
        try {
            snsTemplate.convertAndSend(SNS_TOPIC_ARN, paymentRequest);
        } catch (Exception e) {
            savedOrder.setStatus("FAILED");
            repository.save(savedOrder);
            throw new OrderProcessingException("Failed to publish payment event via SNS", e);
        }

        return mapToResponseDTO(savedOrder);
    }

    public OrderResponseDTO updateOrderStatus(UUID id, String status) {
        OrderEntity order = repository.findById(id)
                .orElseThrow(() -> new OrderProcessingException("Order with ID " + id + " not found", null));
        
        order.setStatus(status);
        OrderEntity updated = repository.save(order);
        return mapToResponseDTO(updated);
    }

    private OrderResponseDTO mapToResponseDTO(OrderEntity entity) {
        return new OrderResponseDTO(
                entity.getId(),
                entity.getUserId(),
                entity.getProductId(),
                entity.getStatus()
        );
    }
}
