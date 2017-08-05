package io.gopulu.rateLimiter.service;

import io.gopulu.rateLimiter.cache.CacheStore;
import io.gopulu.rateLimiter.cache.LRUCache;
import io.gopulu.rateLimiter.domain.Hotel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;


@Service
public class HotelService implements IHotelService {
    private final Logger LOGGER = LoggerFactory.getLogger(HotelService.class);
    @Autowired
    private CacheStore cacheStore;


    @Override
    public List<Hotel> hotelsByCityId(String cityId) {
        LRUCache<String, List<Hotel>> hotelCache = cacheStore.getCacheInstance("HOTEL_DB");
        if (null != hotelCache) {
            return hotelCache.get(cityId);
        }
        return Arrays.asList();
    }

    @Override
    public CompletableFuture<CopyOnWriteArrayList<Hotel>> sortAscHotelsOfCityByPrice(String cityId) {


        CompletableFuture<CopyOnWriteArrayList<Hotel>> hotelsCompletableFure = CompletableFuture.supplyAsync(() -> {
            LRUCache<String, CopyOnWriteArrayList<Hotel>> hotelCache = cacheStore.getCacheInstance("HOTEL_DB");
            CopyOnWriteArrayList<Hotel> safeHotelList = hotelCache.get(cityId);
            Collections.sort(safeHotelList, Comparator.comparingDouble(Hotel::getPrice));
            return safeHotelList;

        }, AsyncApiKeyService.executorService);


        return hotelsCompletableFure;
    }


    @Override
    public CompletableFuture<CopyOnWriteArrayList<Hotel>> sortDescHotelsOfCityByPrice(String cityId) {

        CompletableFuture<CopyOnWriteArrayList<Hotel>> hotelsCompletableFure = CompletableFuture.supplyAsync(() -> {
            LRUCache<String, CopyOnWriteArrayList<Hotel>> hotelCache = cacheStore.getCacheInstance("HOTEL_DB");
            CopyOnWriteArrayList<Hotel> safeHotelList = hotelCache.get(cityId);
            Collections.sort(safeHotelList, Comparator.comparingDouble(Hotel::getPrice).reversed());
            return safeHotelList;

        }, AsyncApiKeyService.executorService);


        return hotelsCompletableFure;

    }
}
