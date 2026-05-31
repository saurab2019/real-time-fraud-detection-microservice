package com.saurabh.frauddetection.entity;

import com.saurabh.frauddetection.dto.Decision;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "fraud_results")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FraudResult {
    @Id
    @Column(name = "transaction_id")
    private String transactionId;
    private Integer fraudScore;

    @Enumerated(EnumType.STRING)
    private Decision decision;

    private LocalDateTime processedAt;

    @PrePersist
    public void prePersist() {
        this.processedAt = LocalDateTime.now();
    }
}
