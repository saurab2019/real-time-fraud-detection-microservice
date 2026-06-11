package com.saurabh.frauddetection.rule;

import com.saurabh.frauddetection.config.properties.VelocityRuleProperties;
import com.saurabh.frauddetection.dto.RuleResult;
import com.saurabh.frauddetection.entity.Transaction;
import com.saurabh.frauddetection.redis.VelocityTrackerService;
import com.saurabh.frauddetection.rules.VelocityRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class VelocityRuleTest {

    @Mock
    private VelocityTrackerService velocityTrackerService;

    private VelocityRule velocityRule;
    private Transaction transaction;

    @BeforeEach
    void setUp()
    {
        VelocityRuleProperties properties = new VelocityRuleProperties(
                5,
                60,
                30
        );

        velocityRule = new VelocityRule(velocityTrackerService, properties);

        transaction = new Transaction();
        transaction.setUserId(123L);
        transaction.setTransactionId("TXN-001");
    }

    @Test
    void shouldReturnScoreWhenTransactionCountGreaterThanMaxTransactions()
    {
        when(
                velocityTrackerService.recordAndGetTransactionCount(
                        transaction.getUserId(), transaction.getTransactionId())
        ).thenReturn(6);

        RuleResult ruleResult = velocityRule.evaluate(transaction);

        assertEquals(30, ruleResult.getScore());
    }

    @Test
    void shouldReturnZeroWhenTransactionCountLessThanMaxTransactions()
    {
        when(
                velocityTrackerService.recordAndGetTransactionCount(
                        transaction.getUserId(), transaction.getTransactionId())
        ).thenReturn(4);

        RuleResult ruleResult = velocityRule.evaluate(transaction);

        assertEquals(0, ruleResult.getScore());
    }

    @Test
    void shouldReturnZeroWhenTransactionCountEqualToMaxTransactions()
    {
        when(
                velocityTrackerService.recordAndGetTransactionCount(
                        transaction.getUserId(), transaction.getTransactionId())
        ).thenReturn(5);

        RuleResult ruleResult = velocityRule.evaluate(transaction);

        assertEquals(0, ruleResult.getScore());
    }
}
