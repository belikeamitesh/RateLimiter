package com.example.ratelimiter.model;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LeakingBucket {
    private final int bucketSize;
    private final int outflowRatePerSecond;
    private final ConcurrentLinkedQueue<Long> queue = new ConcurrentLinkedQueue<>();
    private final Lock lock = new ReentrantLock();

    public LeakingBucket(int bucketSize, int outflowRatePerSecond) {
        this.bucketSize = bucketSize;
        this.outflowRatePerSecond = outflowRatePerSecond;
    }

    public boolean addRequest() {
        lock.lock();
        try {
            long currentTime = System.currentTimeMillis();
            queue.removeIf(timestamp -> currentTime - timestamp > 1000 / outflowRatePerSecond);
            if (queue.size() < bucketSize) {
                queue.add(currentTime);
                return true;
            }
            return false;
        } finally {
            lock.unlock();
        }
    }
}