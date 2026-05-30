package com.saurabh.frauddetection.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "fraud_results")
public class FraudResult {
    @Id
    @Column(name = "transaction_id")
    private String transactionId;
    private Integer fraudScore;
    private String decision;
    private LocalDateTime processedAt;
}
