package com.saurabh.frauddetection.service;

import com.saurabh.frauddetection.dto.RuleResult;
import com.saurabh.frauddetection.entity.AuditLog;
import com.saurabh.frauddetection.repository.IAuditLogRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AuditLogServiceTest {

    @Mock
    private IAuditLogRepository auditLogRepository;

    @InjectMocks
    private AuditLogService auditLogService;


    @Test
    void shouldSaveTriggeredRules() {

        RuleResult triggeredRule = new RuleResult(
                true,
                30,
                "Velocity exceeded",
                "velocityRule"
        );


        List<RuleResult> ruleResultList = new ArrayList<>();
        ruleResultList.add(triggeredRule);

        auditLogService.saveLog(
                "TXN-001",
                ruleResultList
        );

        verify(auditLogRepository).saveAll(anyList());
    }

    @Test
    void shouldNotSaveWhenNoRulesTriggered()
    {
        RuleResult triggeredRule = new RuleResult(
                false,
                0,
                null,
                "velocityRule"
        );


        List<RuleResult> ruleResultList = new ArrayList<>();
        ruleResultList.add(triggeredRule);

        auditLogService.saveLog(
                "TXN-001",
                ruleResultList
        );

        verify(auditLogRepository, never()).saveAll(anyList());
    }

    @Test
    void shouldSaveOnlyTriggeredRules() {

        RuleResult triggeredRule =
                RuleResult.builder()
                        .triggered(true)
                        .score(30)
                        .reason("Velocity exceeded")
                        .ruleName("velocityRule")
                        .build();

        RuleResult nonTriggeredRule =
                RuleResult.builder()
                        .triggered(false)
                        .score(0)
                        .ruleName("amountThresholdRule")
                        .build();

        auditLogService.saveLog(
                "TXN-001",
                List.of(triggeredRule, nonTriggeredRule)
        );

        ArgumentCaptor<List> captor =
                ArgumentCaptor.forClass(List.class);

        verify(auditLogRepository)
                .saveAll(captor.capture());

        List<AuditLog> savedLogs = captor.getValue();
        assertEquals("TXN-001", savedLogs.get(0).getTransactionId());
        assertEquals("velocityRule", savedLogs.get(0).getRuleName());

        assertEquals(1, savedLogs.size());
    }
}
