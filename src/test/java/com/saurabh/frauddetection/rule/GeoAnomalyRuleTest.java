package com.saurabh.frauddetection.rule;

import com.saurabh.frauddetection.config.properties.GeoAnomalyProperties;
import com.saurabh.frauddetection.dto.RuleResult;
import com.saurabh.frauddetection.entity.Transaction;
import com.saurabh.frauddetection.redis.GeoLocationService;
import com.saurabh.frauddetection.rules.GeoAnomalyRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GeoAnomalyRuleTest {

    @Mock
    private GeoLocationService geoLocationService;

    private GeoAnomalyRule geoAnomalyRule;

    @BeforeEach
    void setUp()
    {
        GeoAnomalyProperties properties = new GeoAnomalyProperties(
                50
        );

        geoAnomalyRule = new GeoAnomalyRule(
                geoLocationService,
                properties
        );
    }

    @Test
    void shouldReturnScoreWhenCountryChanges()
    {
        Transaction transaction = new Transaction();
        transaction.setUserId(123L);
        transaction.setCountry("Brazil");

        when(
                geoLocationService.getLastCountry(123L)
        ).thenReturn("India");

        RuleResult ruleResult = geoAnomalyRule.evaluate(transaction);
        assertEquals(50, ruleResult.getScore());
    }

    @Test
    void shouldReturnZeroScoreWhenCountryDoesNotChange()
    {
        Transaction transaction = new Transaction();
        transaction.setUserId(123L);
        transaction.setCountry("Brazil");

        when(
                geoLocationService.getLastCountry(123L)
        ).thenReturn("Brazil");

        RuleResult ruleResult = geoAnomalyRule.evaluate(transaction);
        assertEquals(0, ruleResult.getScore());
    }

    @Test
    void shouldReturnZeroScoreWhenFirstTransaction()
    {
        Transaction transaction = new Transaction();
        transaction.setUserId(123L);
        transaction.setCountry("Brazil");

        when(
                geoLocationService.getLastCountry(123L)
        ).thenReturn(null);

        RuleResult ruleResult = geoAnomalyRule.evaluate(transaction);
        assertEquals(0, ruleResult.getScore());
    }
}
