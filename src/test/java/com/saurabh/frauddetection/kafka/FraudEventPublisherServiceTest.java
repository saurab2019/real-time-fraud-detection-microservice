package com.saurabh.frauddetection.kafka;

import com.saurabh.frauddetection.dto.Decision;
import com.saurabh.frauddetection.logging.IRequestLogger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FraudEventPublisherServiceTest {

    @Mock
    private KafkaTemplate<String, FraudDecisionEvent> kafkaTemplate;

    @Mock
    private IRequestLogger requestLogger;

    @InjectMocks
    private FraudEventPublisherService fraudEventPublisherService;

    @Test
    void shouldPublishFraudEvent() {

        FraudDecisionEvent event =
                new FraudDecisionEvent(
                        "TXN-001",
                        70,
                        Decision.BLOCKED,
                        LocalDateTime.now()
                );

        CompletableFuture<SendResult<String, FraudDecisionEvent>> future =
                new CompletableFuture<>();

        when(
                kafkaTemplate.send(
                        anyString(),
                        anyString(),
                        any(FraudDecisionEvent.class)
                )
        ).thenReturn(future);

        fraudEventPublisherService.publish(event);

        verify(kafkaTemplate)
                .send(
                        KafkaTopics.FRAUD_DECISIONS,
                        "TXN-001",
                        event
                );
    }
}
