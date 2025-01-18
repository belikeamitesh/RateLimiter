package com.example.ratelimiter.model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SlidingWindowLog {
    private final int maxRequests;
    private final long timeWindowMillis;
    private final List<Long> requestLog = new ArrayList<>();
    private final Lock lock = new ReentrantLock();

    public SlidingWindowLog(int maxRequests, long timeWindowMillis) {
        this.maxRequests = maxRequests;
        this.timeWindowMillis = timeWindowMillis;
    }

    public boolean handleRequest() {
        lock.lock();
        try {
            long currentTime = System.currentTimeMillis();
            requestLog.removeIf(timestamp -> currentTime - timestamp > timeWindowMillis);
            if (requestLog.size() < maxRequests) {
                requestLog.add(currentTime);
                return true;
            }
            return false;
        } finally {
            lock.unlock();
        }
    }
}
