package com.saurabh.frauddetection.service;

import com.saurabh.frauddetection.config.properties.DecisionScoreProperties;
import com.saurabh.frauddetection.dto.Decision;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DecisionServiceTest {

    private DecisionService decisionService;

    @BeforeEach
    void setUp() {

        DecisionScoreProperties properties =
                new DecisionScoreProperties(
                        30,
                        70
                );

        decisionService =
                new DecisionService(properties);
    }

    @Test
    void shouldReturnApprovedWhenScoreIsLessThanApproveThreshold() {

        Decision decision =
                decisionService.determineDecision(20);

        assertEquals(
                Decision.APPROVED,
                decision
        );
    }

    @Test
    void shouldReturnApprovedWhenScoreEqualsApproveThreshold() {

        Decision decision =
                decisionService.determineDecision(30);

        assertEquals(
                Decision.APPROVED,
                decision
        );
    }

    @Test
    void shouldReturnOtpRequiredWhenScoreIsBetweenThresholds() {

        Decision decision =
                decisionService.determineDecision(50);

        assertEquals(
                Decision.OTP_REQUIRED,
                decision
        );
    }

    @Test
    void shouldReturnOtpRequiredWhenScoreEqualsOtpThreshold() {

        Decision decision =
                decisionService.determineDecision(70);

        assertEquals(
                Decision.OTP_REQUIRED,
                decision
        );
    }

    @Test
    void shouldReturnBlockedWhenScoreExceedsOtpThreshold() {

        Decision decision =
                decisionService.determineDecision(71);

        assertEquals(
                Decision.BLOCKED,
                decision
        );
    }
}