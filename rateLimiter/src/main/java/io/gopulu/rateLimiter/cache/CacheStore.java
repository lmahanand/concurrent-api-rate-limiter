package io.gopulu.rateLimiter.cache;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CacheStore<K, V> implements ICacheStore<K, V> {

    private final int INITIAL_SIZE = 10000;
    protected static Map<String, LRUCache> cacheMap = new ConcurrentHashMap<>();

    @Override
    public void createInstance(String cacheName) {

        if (null == cacheMap.get(cacheName)) {
            LRUCache lruCache = new LRUCache();
            lruCache.lruCache(INITIAL_SIZE);
            cacheMap.put(cacheName, lruCache);
        }
    }

    @Override
    public LRUCache<K, V> getCacheInstance(String cacheName) {
        return cacheMap.get(cacheName);
    }
}
