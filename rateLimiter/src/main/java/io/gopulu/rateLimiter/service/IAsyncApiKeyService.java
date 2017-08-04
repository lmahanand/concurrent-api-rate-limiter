package io.gopulu.rateLimiter.service;


import io.gopulu.rateLimiter.domain.ApiKey;
import io.gopulu.rateLimiter.domain.RateLimitTracker;

import java.util.concurrent.CompletableFuture;

public interface IAsyncApiKeyService {
    public boolean verifyApiAccessRate(RateLimitTracker rateLimitTracker);
    public void suspendApiKey(ApiKey apiKey);
    public boolean enableApiKey(ApiKey apiKey);
    public void stopPool() throws InterruptedException;
}
