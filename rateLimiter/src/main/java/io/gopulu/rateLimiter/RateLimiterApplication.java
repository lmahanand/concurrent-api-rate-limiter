package io.gopulu.rateLimiter;

import io.gopulu.rateLimiter.cache.CacheStore;
import io.gopulu.rateLimiter.cache.LRUCache;
import io.gopulu.rateLimiter.domain.ApiKey;
import io.gopulu.rateLimiter.domain.City;
import io.gopulu.rateLimiter.domain.Hotel;
import io.gopulu.rateLimiter.domain.ICSVParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

@SpringBootApplication
@EnableAsync
public class RateLimiterApplication {
    private final Logger LOGGER = LoggerFactory.getLogger(RateLimiterApplication.class);
    @Autowired
    private CacheStore cacheStore;

    @Autowired
    private ICSVParser csvParser;


    public static void main(String[] args) {

        SpringApplication.run(RateLimiterApplication.class, args);
    }

    @Bean
    public CommandLineRunner loadData() {
        return (args) -> {
            loadAPIKeysToCache();
            loadHotelsToCache();
        };
    }

    /**
    * Concurrent threads are limited to 4
     * Limit size of the queue is 500
    */
    @Bean
    public Executor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4);
        executor.setMaxPoolSize(4);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("RateLimiterAPI-");
        executor.initialize();
        return executor;
    }

    private void loadAPIKeysToCache() {
        List<ApiKey> apiKeys = csvParser.parseLinesToApiKey();


        cacheStore.createInstance("API-KEYS-CACHE");
        LRUCache<String, ApiKey> keyCache = cacheStore.getCacheInstance("API-KEYS-CACHE");

        LOGGER.info("Loading API KEYS to cache.");

        for (ApiKey key : apiKeys) {
            keyCache.put(key.getKey(), key);
        }


    }

    private void loadHotelsToCache() {
        List<Hotel> hotels = csvParser.parseLinesToHotels("hoteldb.csv");

        cacheStore.createInstance("HOTEL_DB");
        LRUCache<String, List<Hotel>> cache = cacheStore.getCacheInstance("HOTEL_DB");

        Set<String> cities = getCitiesByName(hotels);

        LOGGER.info("Loading hotel csv data to cache.");
        int cityId = 1;
        for (String cityName : cities) {
            List<Hotel> hotelsByCity = getHotelsByCity(cityName, hotels);
            City city = new City(cityId, cityName, hotels);

            cache.put(Integer.toString(cityId), hotelsByCity);

            cityId++;
        }

        LOGGER.info("Data for {} cities loaded to cache.", cityId - 1);
    }

    private List<Hotel> getHotelsByCity(final String city, final List<Hotel> hotels) {
        return hotels.stream().filter(hotel -> hotel.getCity().equals(city)).collect(Collectors.toList());
    }

    private Set<String> getCitiesByName(final List<Hotel> hotels) {
        return hotels.stream().map(hotel -> hotel.getCity()).distinct().collect(Collectors.toSet());
    }

}
