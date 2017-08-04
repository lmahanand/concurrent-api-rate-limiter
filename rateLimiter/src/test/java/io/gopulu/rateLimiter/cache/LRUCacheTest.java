package io.gopulu.rateLimiter.cache;


import org.junit.Assert;
import org.junit.Test;

public class LRUCacheTest {
    @Test
    public void testMethodLruCache(){

        //Given:
        LRUCache<String,String> cache = new LRUCache<>();

        //When:
        cache.lruCache(10);

        cache.put("1","Bangkok");
        cache.put("2","Amsterdam");

        //Then:
        Assert.assertEquals("Bangkok",cache.get("1"));
        Assert.assertEquals("Amsterdam",cache.get("2"));

        Assert.assertNotEquals("Bangkok",cache.get("2"));
    }
}
