package com.saurabh.frauddetection.redis;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GeoLocationServiceTest {

    @Mock
    private StringRedisTemplate stringRedisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @InjectMocks
    private GeoLocationService geoLocationService;

    @Test
    void shouldReturnLastCountry() {

        when(stringRedisTemplate.opsForValue())
                .thenReturn(valueOperations);

        when(valueOperations.get("geo:user:123:last-country"))
                .thenReturn("India");

        String country =
                geoLocationService.getLastCountry(123L);

        assertEquals("India", country);

        verify(valueOperations)
                .get("geo:user:123:last-country");
    }

    @Test
    void shouldUpdateCountry() {

        when(stringRedisTemplate.opsForValue())
                .thenReturn(valueOperations);

        geoLocationService.updateCountry(
                123L,
                "India"
        );

        verify(valueOperations)
                .set(
                        eq("geo:user:123:last-country"),
                        eq("India"),
                        eq(Duration.ofDays(30))
                );
    }
}