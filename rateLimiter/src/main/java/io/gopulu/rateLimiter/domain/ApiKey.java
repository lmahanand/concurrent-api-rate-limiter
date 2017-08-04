package io.gopulu.rateLimiter.domain;

import sun.misc.Contended;

public class ApiKey {
    private final String key;

    @Contended
    private volatile long lastAccessTime;

    private RateLimitTracker limitTracker;

    @Contended
    private volatile boolean active;

    private final int rateLimit;

    public ApiKey(String key, long lastAccessTime, RateLimitTracker limitTracker, boolean active, int rateLimit) {
        this.key = key;
        this.lastAccessTime = lastAccessTime;
        this.limitTracker = limitTracker;
        this.active = active;
        this.rateLimit = rateLimit;
    }

    public int getRateLimit() {
        return rateLimit;
    }

    public void setLastAccessTime(long lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }

    public void setLimitTracker(RateLimitTracker limitTracker) {
        this.limitTracker = limitTracker;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getKey() {

        return key;
    }

    public long getLastAccessTime() {
        return lastAccessTime;
    }

    public RateLimitTracker getLimitTracker() {
        return limitTracker;
    }

    public boolean isActive() {
        return active;
    }

    @Override
    public String toString() {
        return "ApiKey{" +
                "key='" + key + '\'' +
                ", lastAccessTime=" + lastAccessTime +
                ", active=" + active +
                ", rateLimit=" + rateLimit +
                '}';
    }
}
