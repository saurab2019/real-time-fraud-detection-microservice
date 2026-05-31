package com.saurabh.frauddetection.service;

import com.saurabh.frauddetection.dto.Decision;
import com.saurabh.frauddetection.entity.FraudResult;
import com.saurabh.frauddetection.entity.Transaction;

public interface IFraudResultService {
    public FraudResult saveResult(String transactionId,
                                  int score,
                                  Decision decision);
}
