package com.saurabh.frauddetection.service;

import com.saurabh.frauddetection.dto.RuleResult;
import com.saurabh.frauddetection.entity.AuditLog;
import com.saurabh.frauddetection.entity.FraudResult;
import com.saurabh.frauddetection.repository.IAuditLogRepository;
import lombok.AllArgsConstructor;
import org.apache.tomcat.util.digester.Rule;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class AuditLogService implements IAuditLogService{

    private final IAuditLogRepository auditLogRepository;

    public void saveLog(String transactionId, List<RuleResult> ruleResultList)
    {
        List<AuditLog> auditLogs = new ArrayList<>();
        for(RuleResult ruleResult : ruleResultList)
        {
            if(ruleResult.isTriggered())
            {
                AuditLog auditLog = new AuditLog(ruleResult);
                auditLog.setTransactionId(transactionId);
                auditLogs.add(auditLog);
            }
        }
        if(!auditLogs.isEmpty())
        {
            auditLogRepository.saveAll(auditLogs);
        }
    }
}
