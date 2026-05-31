package com.saurabh.frauddetection.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FraudEvaluationResult {
    private int totalScore;
    private List<RuleResult> ruleResults;
}
