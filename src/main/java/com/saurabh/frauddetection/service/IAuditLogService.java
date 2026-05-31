package com.saurabh.frauddetection.service;

import com.saurabh.frauddetection.dto.RuleResult;

import java.util.List;

public interface IAuditLogService {
    public void saveLog(String transactionId, List<RuleResult> ruleResultList);
}
