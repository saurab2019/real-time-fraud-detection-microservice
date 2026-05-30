package com.saurabh.frauddetection.Interfaces;

import com.saurabh.frauddetection.dto.TransactionRequest;
import com.saurabh.frauddetection.dto.TransactionResponse;

public interface ITransactionService {
    public TransactionResponse saveTransaction(TransactionRequest request);
}
