package com.saurabh.frauddetection.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction    {
    @Id
    @Column(name = "transaction_id")
    private String transactionId;
    private Long userId;
    private BigDecimal amount;
    private String country;
    private String merchantId;
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
