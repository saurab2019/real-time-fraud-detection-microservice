package com.saurabh.frauddetection.kafka;

import com.saurabh.frauddetection.dto.Decision;

import java.time.LocalDateTime;

public record FraudDecisionEvent(String transactionId,
                                 Integer score,
                                 Decision decision,
                                 LocalDateTime createdAt) {}
