package com.saurabh.frauddetection.redis;

public interface IGeoLocationService {
    String getLastCountry(Long userId);

    void updateCountry(Long userId, String country);
}
