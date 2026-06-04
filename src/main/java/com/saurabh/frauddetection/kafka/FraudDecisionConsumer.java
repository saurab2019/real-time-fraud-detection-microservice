package com.saurabh.frauddetection.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class FraudDecisionConsumer {

    @KafkaListener(topics = "fraud-decisions", groupId = "fraud-audit-group")
    public void consume(FraudDecisionEvent event)
    {
        log.info("Received fraud decision event: {}", event);
    }
}
