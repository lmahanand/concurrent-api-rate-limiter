package io.gopulu.rateLimiter.service;


import io.gopulu.rateLimiter.domain.Hotel;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;

public interface IHotelService {
    public List<Hotel> hotelsByCityId(final String cityId);
    public CompletableFuture<CopyOnWriteArrayList<Hotel>> sortAscHotelsOfCityByPrice(final String cityId);
    public CompletableFuture<CopyOnWriteArrayList<Hotel>> sortDescHotelsOfCityByPrice(final String cityId);
}
