package com.saurabh.frauddetection.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class Transaction    {
    @Id
    @Column(name = "transaction_id")
    private String transactionId;
    private Long userId;
    private BigDecimal amount;
    private String country;
    private String merchantId;
    private LocalDateTime createdAt;
}
