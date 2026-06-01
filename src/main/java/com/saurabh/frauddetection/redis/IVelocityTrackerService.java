package com.saurabh.frauddetection.redis;

public interface IVelocityTrackerService {
    int recordAndGetTransactionCount(Long userId, String transactionId);
}
