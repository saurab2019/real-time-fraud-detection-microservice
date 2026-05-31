package com.saurabh.frauddetection.service;

import com.saurabh.frauddetection.dto.Decision;
import com.saurabh.frauddetection.dto.FraudEvaluationResult;
import com.saurabh.frauddetection.dto.TransactionRequest;
import com.saurabh.frauddetection.dto.TransactionResponse;
import com.saurabh.frauddetection.engine.FraudDetectionEngine;
import com.saurabh.frauddetection.entity.FraudResult;
import com.saurabh.frauddetection.entity.Transaction;
import com.saurabh.frauddetection.repository.ITransactionRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TransactionService implements ITransactionService {
    private final ITransactionRepository transactionRepository;
    private final IDecisionService decisionService;
    private final IFraudResultService fraudResultService;
    private final IAuditLogService auditLogService;
    private final FraudDetectionEngine fraudDetectionEngine;

    public TransactionService(ITransactionRepository transactionRepository,
                              IDecisionService decisionService,
                              IFraudResultService fraudResultService,
                              IAuditLogService auditLogService,
                              FraudDetectionEngine fraudDetectionEngine)
    {
        this.transactionRepository = transactionRepository;
        this.decisionService = decisionService;
        this.auditLogService = auditLogService;
        this.fraudResultService = fraudResultService;
        this.fraudDetectionEngine = fraudDetectionEngine;
    }

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

        FraudEvaluationResult fraudEvaluationResult = fraudDetectionEngine.evaluate(transaction);
        Decision decision = decisionService.determineDecision(fraudEvaluationResult.getTotalScore());

        FraudResult fraudResult = fraudResultService.saveResult(transaction.getTransactionId(),fraudEvaluationResult.getTotalScore(), decision);

        auditLogService.saveLog(transaction.getTransactionId(), fraudEvaluationResult.getRuleResults());

        return TransactionResponse.builder()
                .transactionId(fraudResult.getTransactionId())
                .score(fraudResult.getFraudScore())
                .decision(fraudResult.getDecision())
                .build();
    }
}
