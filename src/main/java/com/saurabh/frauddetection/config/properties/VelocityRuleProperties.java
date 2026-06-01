package com.saurabh.frauddetection.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "fraud.rules.velocity")
public record VelocityRuleProperties(int maxTransactions,
                                     int windowSeconds,
                                     int score) {
}
