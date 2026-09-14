package com.shopflow.domain.order;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "orders")
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "product_id", nullable = false)
    private UUID productId;

    @Column(nullable = false)
    private String status;

    public OrderEntity() {}

    public OrderEntity(UUID userId, UUID productId, String status) {
        this.userId = userId;
        this.productId = productId;
        this.status = status;
    }

    public UUID getId() { 
        return id; }

    public void setId(UUID id) {
         this.id = id; }

    public UUID getUserId() {
         return userId; }

    public void setUserId(UUID userId) {
         this.userId = userId; }

    public UUID getProductId() {
         return productId; }

    public void setProductId(UUID productId) {
         this.productId = productId; }

    public String getStatus() {
         return status; }
         
    public void setStatus(String status) { 
        this.status = status; }
}
