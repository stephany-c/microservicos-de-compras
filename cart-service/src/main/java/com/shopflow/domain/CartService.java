package com.shopflow.domain;

import com.shopflow.domain.dto.CartItemRequestDTO;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.UUID;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    public CartService(CartRepository cartRepository, CartItemRepository cartItemRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
    }

    @Transactional
    public Cart getOrCreateCart(UUID userId) {
        return cartRepository.findByUserIdAndStatus(userId, CartStatus.OPEN)
                .orElseGet(() -> cartRepository.save(new Cart(userId)));
    }

    @Transactional
    public Cart addItemToCart(UUID userId, CartItemRequestDTO dto) {
        Cart cart = getOrCreateCart(userId);

        Optional<CartItem> existingItem = cartItemRepository.findByCartIdAndProductId(cart.getId(), dto.productId());

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + dto.quantity());
            cartItemRepository.save(item);
        } else {
            CartItem newItem = new CartItem(cart, dto.productId(), dto.quantity());
            cart.addItem(newItem);
            cartItemRepository.save(newItem);
        }

        return cart;
    }

    @Transactional
    public Cart removeItemFromCart(UUID userId, UUID productId) {
        Cart cart = cartRepository.findByUserIdAndStatus(userId, CartStatus.OPEN)
                .orElseThrow(() -> new RuntimeException("Carrinho não encontrado para este usuário"));

        CartItem item = cartItemRepository.findByCartIdAndProductId(cart.getId(), productId)
                .orElseThrow(() -> new RuntimeException("Item não encontrado no carrinho"));

        cart.removeItem(item);
        cartItemRepository.delete(item);

        return cartRepository.save(cart);
    }
}
