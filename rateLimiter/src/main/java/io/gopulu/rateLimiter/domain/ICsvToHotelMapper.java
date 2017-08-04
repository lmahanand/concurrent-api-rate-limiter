package io.gopulu.rateLimiter.domain;


import io.gopulu.rateLimiter.domain.Hotel;

import java.util.List;

public interface ICsvToHotelMapper {
    public Hotel mapCSVToHotel(final List<String> row);
}
