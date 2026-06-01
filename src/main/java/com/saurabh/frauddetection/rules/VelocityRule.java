package com.saurabh.frauddetection.rules;

import com.saurabh.frauddetection.config.properties.VelocityRuleProperties;
import com.saurabh.frauddetection.dto.RuleResult;
import com.saurabh.frauddetection.entity.Transaction;
import com.saurabh.frauddetection.redis.IVelocityTrackerService;
import org.springframework.stereotype.Component;

@Component
public class VelocityRule implements IFraudRule{

    private final IVelocityTrackerService velocityTrackerService;
    private final VelocityRuleProperties velocityRuleProperties;

    public VelocityRule(IVelocityTrackerService velocityTrackerService,
                        VelocityRuleProperties velocityRuleProperties)
    {
        this.velocityTrackerService = velocityTrackerService;
        this.velocityRuleProperties = velocityRuleProperties;
    }

    @Override
    public RuleResult evaluate(Transaction transaction) {
        int transactionCount = velocityTrackerService.recordAndGetTransactionCount(
                transaction.getUserId(),
                transaction.getTransactionId());
        if(transactionCount > velocityRuleProperties.maxTransactions())
        {
            return new RuleResult(
                true,
                velocityRuleProperties.score(),
                String.format(
                        "More than %d transactions detected within %d seconds",
                        velocityRuleProperties.maxTransactions(),
                        velocityRuleProperties.windowSeconds()
                ),
                "VelocityRule");
        }
        return new RuleResult(
                false,
                0,
                null,
                "VelocityRule");
    }
}
