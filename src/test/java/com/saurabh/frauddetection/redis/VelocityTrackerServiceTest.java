package com.saurabh.frauddetection.redis;

import com.saurabh.frauddetection.config.properties.VelocityRuleProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class VelocityTrackerServiceTest {

    @Mock
    private StringRedisTemplate stringRedisTemplate;

    @Mock
    ZSetOperations<String, String> setOperations;

    private VelocityTrackerService velocityTrackerService;

    @BeforeEach
    void setUp()
    {
        VelocityRuleProperties properties =
                new VelocityRuleProperties(
                        5,
                        60,
                        30
                );

        velocityTrackerService = new VelocityTrackerService(stringRedisTemplate, properties);
    }

    @Test
    void shouldRecordTransactionAndReturnCount() {

        when(stringRedisTemplate.opsForZSet()).thenReturn(setOperations);
        when(setOperations.zCard("velocity:user:123")).thenReturn(4L);

        int count = velocityTrackerService.recordAndGetTransactionCount(123L, "TXN-001");

        assertEquals(4, count);
        verify(setOperations).add(
                eq("velocity:user:123"),
                eq("TXN-001"),
                anyDouble()
        );

        verify(setOperations).removeRangeByScore(
                eq("velocity:user:123"),
                anyDouble(),
                anyDouble()
        );

        verify(setOperations)
                .zCard("velocity:user:123");

        verify(stringRedisTemplate)
                .expire(
                        eq("velocity:user:123"),
                        any(Duration.class)
                );
    }
}
