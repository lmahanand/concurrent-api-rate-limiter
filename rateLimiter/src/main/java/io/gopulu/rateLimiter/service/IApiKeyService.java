package io.gopulu.rateLimiter.service;


import io.gopulu.rateLimiter.domain.ApiKey;

public interface IApiKeyService {
    public boolean isApiKeyValid(final String apiKey);
    public boolean isApiKeyActive(final String apiKey);
    public boolean canApiKeyBeSuspended(final String apiKey);
    public void suspendApiKey(final String apiKey);
}
