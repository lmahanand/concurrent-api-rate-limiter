package io.gopulu.rateLimiter.cache;


public interface ICacheStore <K,V>{
    public void createInstance(final String cacheName);
    public LRUCache<K,V> getCacheInstance(final String cacheName);
}
