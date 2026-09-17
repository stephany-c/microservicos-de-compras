package com.shopflow.inventory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InventoryService {

    private final InventoryRepository repository;

    public InventoryService(InventoryRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void processInventoryForOrder(OrderCreatedEventDTO event) {
        // As our system creates an order with a single product and quantity 1 for now
        String productId = event.productId();
        int quantityToReserve = 1;

        InventoryEntity inventory = repository.findByProductId(productId)
                .orElseGet(() -> {
                    // Create a new inventory record with default stock if it doesn't exist
                    return repository.save(new InventoryEntity(productId, 100)); // 100 is default stock
                });

        if (inventory.getAvailableQuantity() < quantityToReserve) {
            throw new RuntimeException("Insufficient stock for product " + productId);
        }

        inventory.setAvailableQuantity(inventory.getAvailableQuantity() - quantityToReserve);
        repository.save(inventory);
    }
}
