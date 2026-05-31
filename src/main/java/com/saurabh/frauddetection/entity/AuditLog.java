package com.saurabh.frauddetection.entity;

import com.saurabh.frauddetection.dto.RuleResult;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String transactionId;
    private String ruleName;
    private Integer ruleScore;
    private String reason;
    private LocalDateTime createdAt;

    public AuditLog(RuleResult ruleResult)
    {
        ruleName = ruleResult.getRuleName();
        ruleScore = ruleResult.getScore();
        reason = ruleResult.getReason();
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
