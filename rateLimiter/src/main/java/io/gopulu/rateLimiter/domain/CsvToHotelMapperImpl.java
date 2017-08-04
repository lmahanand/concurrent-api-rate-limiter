package io.gopulu.rateLimiter.domain;


import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CsvToHotelMapperImpl implements ICsvToHotelMapper {

    @Override
    public Hotel mapCSVToHotel(List<String> row) {

        return new Hotel.HotelBuilder(row.get(0),row.get(1)).room(row.get(2)).price(Double.parseDouble(row.get(3))).buildHotel();

    }
}
