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
import com.saurabh.frauddetection.kafka.FraudEventPublisherService;
import com.saurabh.frauddetection.kafka.IFraudEventPublisherService;
import com.saurabh.frauddetection.logging.IRequestLogger;
import com.saurabh.frauddetection.repository.ITransactionRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionService implements ITransactionService {
    private final ITransactionRepository transactionRepository;
    private final IDecisionService decisionService;
    private final IFraudResultService fraudResultService;
    private final IAuditLogService auditLogService;
    private final FraudDetectionEngine fraudDetectionEngine;
    private final IFraudEventPublisherService fraudEventPublisherService;
    private final IRequestLogger requestLogger;

    private Transaction createTransaction(TransactionRequest request)
    {
        String transactionId = "TXN-" + UUID.randomUUID().toString().substring(0, 8);

        return Transaction.builder()
                        .transactionId(transactionId)
                        .amount(request.getAmount())
                        .country(request.getCountry())
                        .userId(request.getUserId())
                        .merchantId(request.getMerchantId())
                        .build();

    }
    public TransactionResponse  saveTransaction(TransactionRequest request)
    {
        Transaction transaction = transactionRepository.save(createTransaction(request));
        requestLogger.info(transaction.getTransactionId(), "Recieved");

        FraudEvaluationResult fraudEvaluationResult = fraudDetectionEngine.evaluate(transaction);
        Decision decision = decisionService.determineDecision(fraudEvaluationResult.getTotalScore());
        requestLogger.info(transaction.getTransactionId(),
                String.format("Total score: %d, decision: %s",
                fraudEvaluationResult.getTotalScore(),
                decision.toString()));

        FraudResult fraudResult = fraudResultService.saveResult(transaction.getTransactionId(),fraudEvaluationResult.getTotalScore(), decision);
        auditLogService.saveLog(transaction.getTransactionId(), fraudEvaluationResult.getRuleResults());

        fraudEventPublisherService.publish(new FraudDecisionEvent(
                fraudResult.getTransactionId(),
                fraudResult.getFraudScore(),
                fraudResult.getDecision(),
                LocalDateTime.now())
        );

        return TransactionResponse.builder()
                .transactionId(fraudResult.getTransactionId())
                .score(fraudResult.getFraudScore())
                .decision(fraudResult.getDecision())
                .build();
    }

    public Transaction getTransaction(String transactionId)
    {
        return transactionRepository.findById(transactionId)
                .orElseThrow(() -> new TransactionNotFoundException(transactionId));
    }
}
