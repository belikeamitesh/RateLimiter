package com.example.ratelimiter.service;

import com.example.ratelimiter.model.*;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class RateLimiterService {

    private final ConcurrentHashMap<Integer, FixedWindowCounter> fixedWindowLimiters = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Integer, LeakingBucket> leakingBucketLimiters = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Integer, SlidingWindowLog> slidingWindowLimiters = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Integer, TokenBucket> tokenBucketLimiters = new ConcurrentHashMap<>();

    // Fixed Window Rate Limiter
    public boolean handleFixedWindowRequest(int userId) {
        return handleFixedWindowRequest(userId, 3, 1000); // Default values
    }

    public boolean handleFixedWindowRequest(int userId, int maxRequests, long windowDurationMillis) {
        fixedWindowLimiters.putIfAbsent(userId, new FixedWindowCounter(maxRequests, windowDurationMillis));
        return fixedWindowLimiters.get(userId).handleRequest();
    }

    // Leaking Bucket Rate Limiter
    public boolean handleLeakingBucketRequest(int userId) {
        return handleLeakingBucketRequest(userId, 10, 2); // Default values
    }

    public boolean handleLeakingBucketRequest(int userId, int bucketSize, int outflowRatePerSecond) {
        leakingBucketLimiters.putIfAbsent(userId, new LeakingBucket(bucketSize, outflowRatePerSecond));
        return leakingBucketLimiters.get(userId).addRequest();
    }

    // Sliding Window Log Rate Limiter
    public boolean handleSlidingWindowRequest(int userId) {
        return handleSlidingWindowRequest(userId, 3, 1000); // Default values
    }

    public boolean handleSlidingWindowRequest(int userId, int maxRequests, long timeWindowMillis) {
        slidingWindowLimiters.putIfAbsent(userId, new SlidingWindowLog(maxRequests, timeWindowMillis));
        return slidingWindowLimiters.get(userId).handleRequest();
    }

    // Token Bucket Rate Limiter
    public boolean handleTokenBucketRequest(int userId) {
        return handleTokenBucketRequest(userId, 10, 1); // Default values
    }

    public boolean handleTokenBucketRequest(int userId, int capacity, int refillRate) {
        tokenBucketLimiters.putIfAbsent(userId, new TokenBucket(capacity, refillRate));
        return tokenBucketLimiters.get(userId).handleRequest();
    }
}