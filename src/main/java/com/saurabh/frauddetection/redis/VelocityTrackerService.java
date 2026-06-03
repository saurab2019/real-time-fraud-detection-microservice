package com.saurabh.frauddetection.redis;

import com.saurabh.frauddetection.config.properties.VelocityRuleProperties;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class VelocityTrackerService implements IVelocityTrackerService{

    private final StringRedisTemplate stringRedisTemplate;
    private final VelocityRuleProperties velocityRuleProperties;

    @Override
    public int recordAndGetTransactionCount(Long userId, String transactionId) {

        String key = "velocity:user:" + userId;
        long currentTimestamp = Instant.now().getEpochSecond();
        long cutoffTimestamp = currentTimestamp - velocityRuleProperties.windowSeconds();

        ZSetOperations<String, String> zset = stringRedisTemplate.opsForZSet();
        // Remove entries outside sliding window
        zset.removeRangeByScore(
                key,
                0,
                cutoffTimestamp
        );

        // Add current transaction
        zset.add(
                key,
                transactionId,
                currentTimestamp
        );

        // Count transactions within window
        Long count = zset.zCard(key);

        // Cleanup key automatically
        stringRedisTemplate.expire(
                key,
                Duration.ofSeconds(
                        velocityRuleProperties.windowSeconds() * 2L
                )
        );

        return count == null ? 0 : count.intValue();
    }
}
