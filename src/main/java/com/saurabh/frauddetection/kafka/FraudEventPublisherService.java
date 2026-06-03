package com.saurabh.frauddetection.kafka;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FraudEventPublisherService implements IFraudEventPublisherService {

    private final KafkaTemplate<String, FraudDecisionEvent> kafkaTemplate;

    public void publish(FraudDecisionEvent event)
    {
        kafkaTemplate.send(
                KafkaTopics.FRAUD_DECISIONS,
                event.transactionId(),
                event).whenComplete((result, ex) -> {

            if (ex != null) {

                log.error(
                        "Failed to publish fraud event for transaction {}",
                        event.transactionId(),
                        ex);

            } else {
                log.info(
                        "Message published successfully for transaction {}",
                        event.transactionId());
            }
        });

    }
}
