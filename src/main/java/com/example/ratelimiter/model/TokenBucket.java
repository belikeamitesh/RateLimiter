package com.example.ratelimiter.model;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TokenBucket {
    private final int capacity;
    private final int refillRate;
    private int tokens;
    private long lastRefillTimestamp;
    private final Lock lock = new ReentrantLock();

    public TokenBucket(int capacity, int refillRate) {
        this.capacity = capacity;
        this.refillRate = refillRate;
        this.tokens = capacity;
        this.lastRefillTimestamp = System.currentTimeMillis();
    }

    public boolean handleRequest() {
        lock.lock();
        try {
            refill();
            if (tokens > 0) {
                tokens--;
                return true;
            }
            return false;
        } finally {
            lock.unlock();
        }
    }

    private void refill() {
        long currentTime = System.currentTimeMillis();
        long elapsed = currentTime - lastRefillTimestamp;
        int newTokens = (int) (elapsed / 1000) * refillRate;
        tokens = Math.min(capacity, tokens + newTokens);
        lastRefillTimestamp = currentTime;
    }
}