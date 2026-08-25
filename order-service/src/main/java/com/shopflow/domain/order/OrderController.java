package com.shopflow.domain.order;

import com.shopflow.domain.order.dto.OrderRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;
import org.springframework.web.client.RestTemplate;
import com.shopflow.domain.order.dto.ProductClientDTO;
import com.shopflow.domain.order.dto.PaymentRequestDTO;
import io.awspring.cloud.sns.core.SnsTemplate;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderRepository repository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private SnsTemplate snsTemplate;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Order>> getUserOrders(@PathVariable UUID userId) {
        return ResponseEntity.ok(repository.findByUserId(userId));
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody OrderRequestDTO dto) {
        // 1. Fetch product price
        String productUrl = "http://localhost:8081/products/" + dto.productId();
        ProductClientDTO product;
        try {
            product = restTemplate.getForObject(productUrl, ProductClientDTO.class);
            if (product == null) {
                return ResponseEntity.badRequest().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }

        // 2. Create order as PENDING
        Order order = new Order(dto.userId(), dto.productId(), "PENDING");
        Order savedOrder = repository.save(order);

        // 3. Process payment via SNS Event
        PaymentRequestDTO paymentRequest = new PaymentRequestDTO(savedOrder.getId(), product.preco());
        try {
            snsTemplate.convertAndSend("arn:aws:sns:us-east-1:000000000000:order-created-topic", paymentRequest);
        } catch (Exception e) {
            savedOrder.setStatus("FAILED");
            repository.save(savedOrder);
            return ResponseEntity.status(500).build();
        }

        return ResponseEntity.ok(savedOrder);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Order> updateOrderStatus(@PathVariable UUID id, @RequestParam String status) {
        return repository.findById(id).map(order -> {
            order.setStatus(status);
            Order updated = repository.save(order);
            return ResponseEntity.ok(updated);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
