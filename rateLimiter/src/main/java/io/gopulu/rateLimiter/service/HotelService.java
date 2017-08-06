package io.gopulu.rateLimiter.service;

import io.gopulu.rateLimiter.cache.CacheStore;
import io.gopulu.rateLimiter.cache.LRUCache;
import io.gopulu.rateLimiter.domain.Hotel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.*;


@Service
public class HotelService implements IHotelService {
    private final Logger LOGGER = LoggerFactory.getLogger(HotelService.class);
    @Autowired
    private CacheStore cacheStore;

    protected static ExecutorService executorService;

    @Value("${THREADS}")
    private int nThreads;

    @PostConstruct
    public void init() {
        executorService = Executors.newFixedThreadPool(nThreads);
    }

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

        }, executorService);


        return hotelsCompletableFure;
    }


    @Override
    public CompletableFuture<CopyOnWriteArrayList<Hotel>> sortDescHotelsOfCityByPrice(String cityId) {

        CompletableFuture<CopyOnWriteArrayList<Hotel>> hotelsCompletableFure = CompletableFuture.supplyAsync(() -> {
            LRUCache<String, CopyOnWriteArrayList<Hotel>> hotelCache = cacheStore.getCacheInstance("HOTEL_DB");
            CopyOnWriteArrayList<Hotel> safeHotelList = hotelCache.get(cityId);
            Collections.sort(safeHotelList, Comparator.comparingDouble(Hotel::getPrice).reversed());
            return safeHotelList;

        }, executorService);


        return hotelsCompletableFure;

    }

    public void stopPool() throws InterruptedException {
        executorService.shutdown();
        executorService.awaitTermination(10, TimeUnit.SECONDS);
    }

    public void createExecutorService(int nThreads){
        executorService = Executors.newFixedThreadPool(nThreads);
    }
}
