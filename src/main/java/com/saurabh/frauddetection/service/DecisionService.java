package com.saurabh.frauddetection.service;

import com.saurabh.frauddetection.config.properties.DecisionScoreProperties;
import com.saurabh.frauddetection.dto.Decision;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DecisionService implements IDecisionService{

    private final DecisionScoreProperties properties;

    public Decision determineDecision(int score)
    {
        Decision decision = Decision.BLOCKED;
        if(score <= properties.approveMaxScore())
        {
            decision = Decision.APPROVED;
        }
        else if(score <= properties.otpMaxScore()) {
            decision = Decision.OTP_REQUIRED;
        }
        return decision;
    }
}
