package com.saurabh.frauddetection.kafka;

import com.saurabh.frauddetection.logging.IRequestLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FraudDecisionConsumer {

    private final IRequestLogger requestLogger;

    @KafkaListener(topics = "fraud-decisions", groupId = "fraud-audit-group")
    public void consume(FraudDecisionEvent event)
    {
        requestLogger.info(String.format("Received fraud decision event: %s", event.toString()));
    }
}
