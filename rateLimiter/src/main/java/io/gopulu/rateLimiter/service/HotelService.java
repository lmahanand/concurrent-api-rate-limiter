package io.gopulu.rateLimiter.service;

import io.gopulu.rateLimiter.cache.CacheStore;
import io.gopulu.rateLimiter.cache.LRUCache;
import io.gopulu.rateLimiter.domain.Hotel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


@Service
public class HotelService implements IHotelService{
    @Autowired
    private CacheStore cacheStore;
    @Override
    public List<Hotel> hotelsByCityId(String cityId) {
        LRUCache<String,List<Hotel>> hotelCache = cacheStore.getCacheInstance("HOTEL_DB");
        if(null != hotelCache){
            return hotelCache.get(cityId);
        }
        return Arrays.asList();
    }

    @Override
    public List<Hotel> sortAscHotelsOfCityByPrice(String cityId) {
        LRUCache<String,List<Hotel>> hotelCache = cacheStore.getCacheInstance("HOTEL_DB");
        if(null != hotelCache){
            List<Hotel> hotels = hotelCache.get(cityId);
            Collections.sort(hotels, Comparator.comparingDouble(Hotel::getPrice));
            return hotels;
        }

        return Arrays.asList();
    }

    @Override
    public List<Hotel> sortDescHotelsOfCityByPrice(String cityId) {

        LRUCache<String,List<Hotel>> hotelCache = cacheStore.getCacheInstance("HOTEL_DB");
        if(null != hotelCache){
            List<Hotel> hotels = hotelCache.get(cityId);
            Collections.sort(hotels, Comparator.comparingDouble(Hotel::getPrice).reversed());

            return hotels;
        }

        return Arrays.asList();
    }
}
