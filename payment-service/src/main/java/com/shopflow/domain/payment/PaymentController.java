package com.shopflow.domain.payment;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    @PostMapping("/process")
    public ResponseEntity<String> processPayment(@RequestBody PaymentRequestDTO dto) {
        if (dto.amount() <= 0) {
            return ResponseEntity.badRequest().body("Invalid amount");
        }
        return ResponseEntity.ok("Payment processed successfully for order: " + dto.orderId());
    }
}
