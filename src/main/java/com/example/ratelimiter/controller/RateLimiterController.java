package com.example.ratelimiter.controller;

import com.example.ratelimiter.service.RateLimiterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RateLimiterController {

    @Autowired
    private RateLimiterService rateLimiterService;

    @GetMapping("/fixed-window")
    public String testFixedWindow(
            @RequestParam int userId,
            @RequestParam(required = false, defaultValue = "3") int maxRequests,
            @RequestParam(required = false, defaultValue = "1000") long windowDurationMillis) {
        boolean allowed = rateLimiterService.handleFixedWindowRequest(userId, maxRequests, windowDurationMillis);
        return allowed ? "Request allowed" : "Too many requests. Try later.";
    }

    @GetMapping("/leaking-bucket")
    public String testLeakingBucket(
            @RequestParam int userId,
            @RequestParam(required = false, defaultValue = "10") int bucketSize,
            @RequestParam(required = false, defaultValue = "2") int outflowRatePerSecond) {
        boolean allowed = rateLimiterService.handleLeakingBucketRequest(userId, bucketSize, outflowRatePerSecond);
        return allowed ? "Request allowed" : "Too many requests. Try later.";
    }

    @GetMapping("/sliding-window")
    public String testSlidingWindow(
            @RequestParam int userId,
            @RequestParam(required = false, defaultValue = "3") int maxRequests,
            @RequestParam(required = false, defaultValue = "1000") long timeWindowMillis) {
        boolean allowed = rateLimiterService.handleSlidingWindowRequest(userId, maxRequests, timeWindowMillis);
        return allowed ? "Request allowed" : "Too many requests. Try later.";
    }

    @GetMapping("/token-bucket")
    public String testTokenBucket(
            @RequestParam int userId,
            @RequestParam(required = false, defaultValue = "10") int capacity,
            @RequestParam(required = false, defaultValue = "1") int refillRate) {
        boolean allowed = rateLimiterService.handleTokenBucketRequest(userId, capacity, refillRate);
        return allowed ? "Request allowed" : "Too many requests. Try later.";
    }
}