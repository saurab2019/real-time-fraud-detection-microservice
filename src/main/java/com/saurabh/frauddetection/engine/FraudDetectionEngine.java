package com.saurabh.frauddetection.engine;

import com.saurabh.frauddetection.dto.FraudEvaluationResult;
import com.saurabh.frauddetection.dto.RuleResult;
import com.saurabh.frauddetection.entity.Transaction;
import com.saurabh.frauddetection.rules.IFraudRule;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class FraudDetectionEngine {

    private final List<IFraudRule> fraudRuleList;

    public FraudDetectionEngine(List<IFraudRule> fraudRuleList)
    {
        this.fraudRuleList = fraudRuleList;
    }

    public FraudEvaluationResult evaluate(Transaction transaction)
    {
        int totalScore = 0;
        List<RuleResult> ruleResultList = new ArrayList<>();

        for(IFraudRule fraudRule : fraudRuleList)
        {
            RuleResult result = fraudRule.evaluate(transaction);
            ruleResultList.add(result);
            totalScore += result.getScore();
        }
        return new FraudEvaluationResult(totalScore, ruleResultList);
    }


}
