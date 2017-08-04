package io.gopulu.rateLimiter.service;


import io.gopulu.rateLimiter.domain.Hotel;

import java.util.List;

public interface IHotelService {
    public List<Hotel> hotelsByCityId(final String cityId);
    public List<Hotel> sortAscHotelsOfCityByPrice(final String cityId);
    public List<Hotel> sortDescHotelsOfCityByPrice(final String cityId);
}
