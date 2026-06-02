package com.saurabh.frauddetection.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "fraud.rules.geo-anomaly")
public record GeoAnomalyProperties(int score) {
}
