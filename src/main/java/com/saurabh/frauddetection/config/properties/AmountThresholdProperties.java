package com.saurabh.frauddetection.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.math.BigDecimal;

@ConfigurationProperties(prefix = "fraud.rules.amount-threshold")
public record AmountThresholdProperties(BigDecimal threshold, Integer score) {
}
