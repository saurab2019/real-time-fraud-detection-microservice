package com.saurabh.frauddetection.rule;

import com.saurabh.frauddetection.config.properties.AmountThresholdProperties;
import com.saurabh.frauddetection.dto.RuleResult;
import com.saurabh.frauddetection.entity.Transaction;
import com.saurabh.frauddetection.rules.AmountThresholdRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class AmountThresholdRuleTest {

    private AmountThresholdRule amountThresholdRule;

    @BeforeEach
    void setUp()
    {
        AmountThresholdProperties properties = new AmountThresholdProperties(
                new BigDecimal(5000),
                40
        );

        amountThresholdRule = new AmountThresholdRule(properties);
    }

    @Test
    void shouldReturnScoreWhenAmountExceedsThreshold()
    {
        Transaction transaction = new Transaction();
        transaction.setAmount(new BigDecimal(6000));

        RuleResult ruleResult = amountThresholdRule.evaluate(transaction);

        assertEquals(40, ruleResult.getScore());
    }

    @Test
    void shouldReturnZeroWhenAmountLessThanThreshold()
    {
        Transaction transaction = new Transaction();
        transaction.setAmount(new BigDecimal(4000));

        RuleResult ruleResult = amountThresholdRule.evaluate(transaction);

        assertEquals(0, ruleResult.getScore());
    }

    @Test
    void shouldReturnZeroWhenAmountEqualToThreshold()
    {
        Transaction transaction = new Transaction();
        transaction.setAmount(new BigDecimal(5000));

        RuleResult ruleResult = amountThresholdRule.evaluate(transaction);

        assertEquals(0, ruleResult.getScore());
    }
}
