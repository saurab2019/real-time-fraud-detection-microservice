package com.saurabh.frauddetection.service;

import com.saurabh.frauddetection.dto.TransactionRequest;
import com.saurabh.frauddetection.dto.TransactionResponse;
import com.saurabh.frauddetection.entity.Transaction;

public interface ITransactionService {
    public TransactionResponse saveTransaction(TransactionRequest request);
    public Transaction getTransaction(String transactionId);
}
