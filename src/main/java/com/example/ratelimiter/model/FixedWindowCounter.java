package com.example.ratelimiter.model;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class FixedWindowCounter {
    private final int maxRequests;
    private final long timeIntervalMillis;
    private long startTime;
    private int requestCount;
    private final Lock lock = new ReentrantLock();

    public FixedWindowCounter(int maxRequests, long timeIntervalMillis) {
        this.maxRequests = maxRequests;
        this.timeIntervalMillis = timeIntervalMillis;
        this.startTime = System.currentTimeMillis();
        this.requestCount = 0;
    }

    public boolean handleRequest() {
        lock.lock();
        try {
            long currentTime = System.currentTimeMillis();
            if (currentTime - startTime > timeIntervalMillis) {
                startTime = currentTime;
                requestCount = 0;
            }
            if (requestCount < maxRequests) {
                requestCount++;
                return true;
            }
            return false;
        } finally {
            lock.unlock();
        }
    }
}