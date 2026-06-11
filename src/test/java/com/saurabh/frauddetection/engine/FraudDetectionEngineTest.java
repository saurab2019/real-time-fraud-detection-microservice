package com.saurabh.frauddetection.engine;

import com.saurabh.frauddetection.dto.Decision;
import com.saurabh.frauddetection.dto.FraudEvaluationResult;
import com.saurabh.frauddetection.dto.RuleResult;
import com.saurabh.frauddetection.entity.Transaction;
import com.saurabh.frauddetection.rules.IFraudRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FraudDetectionEngineTest {

    @Mock
    private IFraudRule rule1;

    @Mock
    private IFraudRule rule2;

    private FraudDetectionEngine fraudDetectionEngine;

    @BeforeEach
    void setUp()
    {
        fraudDetectionEngine = new FraudDetectionEngine(
                List.of(rule1, rule2)
        );
    }

    @Test
    void shouldCalculateTotalScoreFromAllRules() {
        Transaction transaction = new Transaction();

        when(
                rule1.evaluate(transaction)
        ).thenReturn(new RuleResult(
                true,
                40,
                "RULE1 reason",
                "RULE1"
        ));

        when(
                rule2.evaluate(transaction)
        ).thenReturn(new RuleResult(
                true,
                30,
                "RULE2 reason",
                "RULE2"
        ));

        FraudEvaluationResult fraudEvaluationResult = fraudDetectionEngine.evaluate(transaction);
        assertEquals(70, fraudEvaluationResult.getTotalScore());
        assertEquals(2, fraudEvaluationResult.getRuleResults().size());

        verify(rule1).evaluate(transaction);
        verify(rule2).evaluate(transaction);
    }

    @Test
    void shouldReturnZeroScoreWhenNoRulesConfigured() {

        FraudDetectionEngine engine = new FraudDetectionEngine(
                List.of()
        );


        Transaction transaction = new Transaction();

        FraudEvaluationResult result =
                engine.evaluate(transaction);

        assertEquals(0, result.getTotalScore());
        assertEquals(0, result.getRuleResults().size());
    }

    @Test
    void shouldIgnoreNonTriggeredRules() {

        Transaction transaction = new Transaction();

        when(rule1.evaluate(transaction))
                .thenReturn(new RuleResult(
                        true,
                        40,
                        "RULE1 reason",
                        "RULE1"
                ));

        when(rule2.evaluate(transaction))
                .thenReturn(new RuleResult(
                        false,
                        0,
                        "RULE2 reason",
                        "RULE2"
                ));

        FraudEvaluationResult result =
                fraudDetectionEngine.evaluate(transaction);

        assertEquals(40, result.getTotalScore());
        assertEquals(1, result.getRuleResults().size());

        verify(rule1).evaluate(transaction);
        verify(rule2).evaluate(transaction);
    }

    @Test
    void shouldReturnZeroScoreWhenNoRulesTriggered() {

        Transaction transaction = new Transaction();

        when(rule1.evaluate(transaction))
                .thenReturn(new RuleResult(
                        false,
                        0,
                        "RULE1 reason",
                        "RULE1"
                ));

        when(rule2.evaluate(transaction))
                .thenReturn(new RuleResult(
                        false,
                        0,
                        "RULE2 reason",
                        "RULE2"
                ));

        FraudEvaluationResult result =
                fraudDetectionEngine.evaluate(transaction);

        assertEquals(0, result.getTotalScore());
        assertEquals(0, result.getRuleResults().size());

        verify(rule1).evaluate(transaction);
        verify(rule2).evaluate(transaction);
    }
}
