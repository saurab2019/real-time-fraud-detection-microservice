package com.saurabh.frauddetection.service;

import com.saurabh.frauddetection.dto.Decision;
import com.saurabh.frauddetection.dto.FraudEvaluationResult;
import com.saurabh.frauddetection.dto.TransactionRequest;
import com.saurabh.frauddetection.dto.TransactionResponse;
import com.saurabh.frauddetection.engine.FraudDetectionEngine;
import com.saurabh.frauddetection.entity.FraudResult;
import com.saurabh.frauddetection.entity.Transaction;
import com.saurabh.frauddetection.exception.TransactionNotFoundException;
import com.saurabh.frauddetection.kafka.FraudDecisionEvent;
import com.saurabh.frauddetection.kafka.IFraudEventPublisherService;
import com.saurabh.frauddetection.logging.IRequestLogger;
import com.saurabh.frauddetection.repository.ITransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Mock
    private ITransactionRepository transactionRepository;

    @Mock
    private IDecisionService decisionService;

    @Mock
    private IFraudResultService fraudResultService;

    @Mock
    private IAuditLogService auditLogService;

    @Mock
    private FraudDetectionEngine fraudDetectionEngine;

    @Mock
    private IFraudEventPublisherService fraudEventPublisherService;

    @Mock
    private IRequestLogger requestLogger;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    void shouldProcessTransactionSuccessfully() {

        TransactionRequest request = new TransactionRequest();
        request.setAmount(new BigDecimal("75000"));
        request.setCountry("India");
        request.setUserId(101L);
        request.setMerchantId("M001");

        Transaction transaction = Transaction.builder()
                .transactionId("TXN-001")
                .build();


        FraudEvaluationResult evaluationResult = new FraudEvaluationResult(
                70,
                List.of()
        );


        FraudResult fraudResult = new FraudResult();
        fraudResult.setTransactionId("TXN-001");
        fraudResult.setFraudScore(70);
        fraudResult.setDecision(Decision.OTP_REQUIRED);

        when(transactionRepository.save(any(Transaction.class)))
                .thenReturn(transaction);

        when(fraudDetectionEngine.evaluate(transaction))
                .thenReturn(evaluationResult);

        when(decisionService.determineDecision(70))
                .thenReturn(Decision.OTP_REQUIRED);

        when(
                fraudResultService.saveResult(
                        "TXN-001",
                        70,
                        Decision.OTP_REQUIRED
                )
        ).thenReturn(fraudResult);

        TransactionResponse response =
                transactionService.saveTransaction(request);

        assertEquals("TXN-001",
                response.getTransactionId());

        assertEquals(70,
                response.getScore());

        assertEquals(
                Decision.OTP_REQUIRED,
                response.getDecision()
        );

        verify(transactionRepository)
                .save(any(Transaction.class));

        verify(fraudDetectionEngine)
                .evaluate(transaction);

        verify(decisionService)
                .determineDecision(70);

        verify(fraudResultService)
                .saveResult(
                        "TXN-001",
                        70,
                        Decision.OTP_REQUIRED
                );

        verify(auditLogService)
                .saveLog(
                        eq("TXN-001"),
                        anyList()
                );

        verify(fraudEventPublisherService)
                .publish(any(FraudDecisionEvent.class));
    }

    @Test
    void shouldReturnTransactionWhenFound()
    {
        Transaction transaction = Transaction.builder()
                .transactionId("TXN-001")
                .userId(123L)
                .country("India")
                .build();

        when(transactionRepository.findById("TXN-001"))
                .thenReturn(Optional.of(transaction));

        Transaction result =
                transactionService.getTransaction("TXN-001");

        assertEquals(
                "TXN-001",
                result.getTransactionId()
        );

        verify(transactionRepository)
                .findById("TXN-001");
    }

    @Test
    void shouldThrowExceptionWhenTransactionNotFound()
    {
        when(transactionRepository.findById("TXN-001"))
                .thenReturn(Optional.empty());

        TransactionNotFoundException exception =
                assertThrows(
                        TransactionNotFoundException.class,
                        () -> transactionService.getTransaction("TXN-001")
                );

        assertTrue(
                exception.getMessage().contains("TXN-001")
        );

        verify(transactionRepository)
                .findById("TXN-001");
    }
}
