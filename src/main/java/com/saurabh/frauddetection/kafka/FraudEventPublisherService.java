package com.saurabh.frauddetection.kafka;

import com.saurabh.frauddetection.logging.IRequestLogger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FraudEventPublisherService implements IFraudEventPublisherService {

    private final KafkaTemplate<String, FraudDecisionEvent> kafkaTemplate;
    private final IRequestLogger requestLogger;

    public void publish(FraudDecisionEvent event)
    {
        kafkaTemplate.send(
                KafkaTopics.FRAUD_DECISIONS,
                event.transactionId(),
                event).whenComplete((result, ex) -> {

            if (ex != null) {
                requestLogger.error(event.transactionId(),"Failed to publish fraud event", ex);

            } else {
                requestLogger.info(event.transactionId(), "Message published successfully");
            }
        });

    }
}
