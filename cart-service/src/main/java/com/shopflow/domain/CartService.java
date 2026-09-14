package com.shopflow.domain;

import com.shopflow.domain.dto.CartItemRequestDTO;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.List;
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
    public CartEntity getOrCreateCart(UUID userId) {
        return cartRepository.findByUserIdAndStatus(userId, CartStatus.OPEN)
                .orElseGet(() -> cartRepository.save(new CartEntity(userId)));
    }

    @Transactional
    public CartEntity addItemToCart(UUID userId, CartItemRequestDTO dto) {
        CartEntity cart = getOrCreateCart(userId);

        List<CartItemEntity> existingItems = cartItemRepository.findByCartIdAndProductId(cart.getId(), dto.productId());

        if (!existingItems.isEmpty()) {
            CartItemEntity item = existingItems.get(0);
            item.setQuantity(item.getQuantity() + dto.quantity());
            cartItemRepository.save(item);
            
            if (existingItems.size() > 1) {
                for (int i = 1; i < existingItems.size(); i++) {
                    cart.removeItem(existingItems.get(i));
                    cartItemRepository.delete(existingItems.get(i));
                }
            }
        } else {
            CartItemEntity newItem = new CartItemEntity(cart, dto.productId(), dto.quantity());
            cart.addItem(newItem);
            cartItemRepository.save(newItem);
        }

        return cart;
    }

    @Transactional
    public CartEntity removeItemFromCart(UUID userId, UUID productId) {
        CartEntity cart = cartRepository.findByUserIdAndStatus(userId, CartStatus.OPEN)
                .orElseThrow(() -> new RuntimeException("Carrinho não encontrado para este usuário"));

        List<CartItemEntity> items = cartItemRepository.findByCartIdAndProductId(cart.getId(), productId);
        if (items.isEmpty()) {
            throw new RuntimeException("Item não encontrado no carrinho");
        }

        for (CartItemEntity item : items) {
            cart.removeItem(item);
            cartItemRepository.delete(item);
        }

        return cartRepository.save(cart);
    }
}
