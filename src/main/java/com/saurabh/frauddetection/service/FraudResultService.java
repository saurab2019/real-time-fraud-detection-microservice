package com.saurabh.frauddetection.service;

import com.saurabh.frauddetection.dto.Decision;
import com.saurabh.frauddetection.entity.FraudResult;
import com.saurabh.frauddetection.repository.IFraudResultRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class FraudResultService implements IFraudResultService {
    private final IFraudResultRepository fraudResultRepository;

    public FraudResult saveResult(String transactionId,
                                  int score,
                                  Decision decision)
    {
        FraudResult fraudResult = new FraudResult();
        fraudResult.setTransactionId(transactionId);
        fraudResult.setFraudScore(score);
        fraudResult.setDecision(decision);

        return fraudResultRepository.save(fraudResult);
    }
}
