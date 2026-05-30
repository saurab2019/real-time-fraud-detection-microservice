package com.saurabh.frauddetection.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
public class AuditLog {
    @Id
    private Long id;
    private String transactionId;
    private String ruleName;
    private Integer ruleScore;
    private String reason;
    private LocalDateTime createdAt;
}
