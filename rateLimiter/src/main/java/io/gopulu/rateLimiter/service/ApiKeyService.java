package io.gopulu.rateLimiter.service;

import io.gopulu.rateLimiter.cache.CacheStore;
import io.gopulu.rateLimiter.cache.LRUCache;
import io.gopulu.rateLimiter.domain.ApiKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ApiKeyService implements IApiKeyService{
    @Value("API-KEYS-CACHE")
    private String apiKeyCacheName;

    //@Value("20000")
    @Value("${SUSPEND-DURATION}")
    private int suspendDuration;

    @Autowired
    private IAsyncApiKeyService asyncApiKeyService;

    @Autowired
    private CacheStore cacheStore;

    @Override
    public boolean isApiKeyValid(String apiKey) {
        LRUCache<String,ApiKey> apiKeys = getApiKeyCache();
        if(null == apiKeys || apiKeys.size()==0){
            return false;
        }
        return null != apiKeys.get(apiKey);
    }

    @Override
    public boolean isApiKeyActive(String apiKey) {
        LRUCache<String,ApiKey> apiKeys = getApiKeyCache();
        if(null == apiKeys || apiKeys.size()==0){
            return false;
        }

        ApiKey key = apiKeys.get(apiKey);

        long lastAccesstime = key.getLastAccessTime();
        long currentTime = System.currentTimeMillis();

        if(suspendDuration <= (currentTime - lastAccesstime)){
            return asyncApiKeyService.enableApiKey(key);
        }

        return key.isActive();
    }

    @Override
    public boolean canApiKeyBeSuspended(String apiKey) {
        LRUCache<String,ApiKey> apiKeys = getApiKeyCache();
        if(null == apiKeys || apiKeys.size()==0){
            return false;
        }
        boolean flag = asyncApiKeyService.verifyApiAccessRate(apiKeys.get(apiKey).getLimitTracker());
        return flag;
    }

    @Override
    public void suspendApiKey(String apiKey) {
        LRUCache<String,ApiKey> apiKeys = getApiKeyCache();
        if(null != apiKeys || apiKeys.size()>0){
            asyncApiKeyService.suspendApiKey(apiKeys.get(apiKey));
        }
    }

    private LRUCache<String,ApiKey> getApiKeyCache(){
        return cacheStore.getCacheInstance(apiKeyCacheName);
    }
}
