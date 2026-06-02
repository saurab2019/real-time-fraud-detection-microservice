package com.saurabh.frauddetection.redis;

import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@AllArgsConstructor
public class GeoLocationService implements IGeoLocationService{

    private final StringRedisTemplate stringRedisTemplate;
    private final String KEY_PREFIX = "geo:user:";
    @Override
    public String getLastCountry(Long userId) {
        return stringRedisTemplate.opsForValue()
                .get(KEY_PREFIX + userId + ":last-country");

    }

    @Override
    public void updateCountry(Long userId, String country) {
        String key = KEY_PREFIX + userId + ":last-country";
        stringRedisTemplate.opsForValue()
                .set(key, country, Duration.ofDays(30));
    }
}
