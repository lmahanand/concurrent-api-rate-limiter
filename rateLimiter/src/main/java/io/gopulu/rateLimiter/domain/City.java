package io.gopulu.rateLimiter.domain;


import java.util.List;

public class City {
    final private int cityId;
    final private String cityName;
    final private List<Hotel> hotels;

    public City(int cityId, String cityName, List<Hotel> hotels) {
        this.cityId = cityId;
        this.cityName = cityName;
        this.hotels = hotels;
    }

    public int getCityId() {
        return cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public List<Hotel> getHotels() {
        return hotels;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        City city = (City) o;

        if (cityId != city.cityId) return false;
        return cityName != null ? cityName.equals(city.cityName) : city.cityName == null;
    }

    @Override
    public int hashCode() {
        int result = cityId;
        result = 31 * result + (cityName != null ? cityName.hashCode() : 0);
        return result;
    }
}
