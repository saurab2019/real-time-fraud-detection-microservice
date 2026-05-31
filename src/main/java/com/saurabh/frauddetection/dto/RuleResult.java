package com.saurabh.frauddetection.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RuleResult {
    private boolean triggered;
    private int score;
    private String reason;
    private String ruleName;
}
