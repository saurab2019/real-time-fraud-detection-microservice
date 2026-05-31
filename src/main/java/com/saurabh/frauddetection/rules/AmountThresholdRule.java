package com.saurabh.frauddetection.rules;

import com.saurabh.frauddetection.config.properties.AmountThresholdProperties;
import com.saurabh.frauddetection.dto.RuleResult;
import com.saurabh.frauddetection.entity.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class AmountThresholdRule implements IFraudRule{

    private final AmountThresholdProperties properties;

    public AmountThresholdRule(AmountThresholdProperties properties)
    {
        this.properties = properties;
    }

    @Override
    public RuleResult evaluate(Transaction transaction) {
        if(transaction.getAmount().compareTo(properties.threshold()) > 0)
        {
            return new RuleResult(
                    true,
                    properties.score(),
                    "Amount exceeds threshold",
                    "AmountThresholdRule");
        }
        return new RuleResult(
                false,
                0,
                null,
                "AmountThresholdRule");
    }
}
