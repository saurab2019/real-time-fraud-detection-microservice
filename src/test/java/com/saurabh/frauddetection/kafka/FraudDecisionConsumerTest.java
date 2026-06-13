package com.saurabh.frauddetection.kafka;

import com.saurabh.frauddetection.dto.Decision;
import com.saurabh.frauddetection.logging.IRequestLogger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class FraudDecisionConsumerTest {

    @Mock
    private IRequestLogger requestLogger;

    @InjectMocks
    private FraudDecisionConsumer fraudDecisionConsumer;

    @Test
    void shouldLogReceivedEvent() {

        FraudDecisionEvent event =
                new FraudDecisionEvent(
                        "TXN-001",
                        70,
                        Decision.BLOCKED,
                        LocalDateTime.now()
                );

        fraudDecisionConsumer.consume(event);

        verify(requestLogger)
                .info(
                        String.format(
                                "Received fraud decision event: %s",
                                event
                        )
                );
    }
}
