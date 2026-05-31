package com.saurabh.frauddetection.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "fraud.decision")
public record DecisionScoreProperties(int approveMaxScore, int otpMaxScore) {
}
