package com.saurabh.frauddetection.exception;

public class TransactionNotFoundException extends FraudException {

    public TransactionNotFoundException(String transactionId) {
        super("Transaction not found: " + transactionId);
    }
}
