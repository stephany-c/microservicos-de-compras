package com.shopflow.domain;

import com.shopflow.domain.dto.CartItemRequestDTO;
import com.shopflow.domain.dto.CartResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/carts")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<CartResponseDTO> getCart(@PathVariable UUID userId) {
        Cart cart = cartService.getOrCreateCart(userId);
        return ResponseEntity.ok(new CartResponseDTO(cart));
    }

    @PostMapping("/{userId}/items")
    public ResponseEntity<CartResponseDTO> addItem(
            @PathVariable UUID userId,
            @Valid @RequestBody CartItemRequestDTO dto) {
        Cart cart = cartService.addItemToCart(userId, dto);
        return ResponseEntity.ok(new CartResponseDTO(cart));
    }

    @DeleteMapping("/{userId}/items/{productId}")
    public ResponseEntity<CartResponseDTO> removeItem(
            @PathVariable UUID userId,
            @PathVariable UUID productId) {
        Cart cart = cartService.removeItemFromCart(userId, productId);
        return ResponseEntity.ok(new CartResponseDTO(cart));
    }
}
