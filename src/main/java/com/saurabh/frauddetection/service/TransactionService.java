package com.saurabh.frauddetection.service;

import com.saurabh.frauddetection.Interfaces.ITransactionService;
import com.saurabh.frauddetection.dto.TransactionRequest;
import com.saurabh.frauddetection.dto.TransactionResponse;
import com.saurabh.frauddetection.entity.Transaction;
import com.saurabh.frauddetection.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.UUID;

@Service
public class TransactionService implements ITransactionService {
    private TransactionRepository transactionRepository;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository)
    {
        this.transactionRepository = transactionRepository;
    }

    public TransactionResponse  saveTransaction(TransactionRequest request)
    {
        String transactionId = "TXN-" + UUID.randomUUID().toString().substring(0, 8);

        Transaction transaction = Transaction.builder()
                .transactionId(transactionId)
                .amount(request.getAmount())
                .country(request.getCountry())
                .userId(request.getUserId())
                .merchantId(request.getMerchantId())
                .build();

        Transaction saved = transactionRepository.save(transaction);

        return TransactionResponse.builder()
                .transactionId(saved.getTransactionId())
                .status("SUCCESS")
                .build();
    }
}
