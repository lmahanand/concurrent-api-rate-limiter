package io.gopulu.rateLimiter.domain;


public class Hotel {
    private final String city;
    private final String id;
    private final String room;
    private final double price;

    private Hotel(final String city, final String id, final String room, final double price) {
        this.city = city;
        this.id = id;
        this.room = room;
        this.price = price;
    }

    public String getCity() {
        return city;
    }

    public String getId() {
        return id;
    }

    public String getRoom() {
        return room;
    }

    public double getPrice() {
        return price;
    }

    public static class HotelBuilder {
        private String city;
        private String id;
        private String room;
        private double price;

        public HotelBuilder(final String city, final String id) {
            this.city = city;
            this.id = id;
        }

        public HotelBuilder city(final String city) {
            this.city = city;
            return this;
        }

        public HotelBuilder id(final String id) {
            this.id = id;
            return this;
        }

        public HotelBuilder room(final String room) {
            this.room = room;
            return this;
        }

        public HotelBuilder price(double price) {
            this.price = price;
            return this;
        }

        public Hotel buildHotel() {
            return new Hotel(city, id, room, price);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Hotel hotel = (Hotel) o;

        if (Double.compare(hotel.price, price) != 0) return false;
        if (city != null ? !city.equals(hotel.city) : hotel.city != null) return false;
        if (id != null ? !id.equals(hotel.id) : hotel.id != null) return false;
        return room != null ? room.equals(hotel.room) : hotel.room == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = city != null ? city.hashCode() : 0;
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (room != null ? room.hashCode() : 0);
        temp = Double.doubleToLongBits(price);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "Hotel{" +
                "city='" + city + '\'' +
                ", id='" + id + '\'' +
                ", room='" + room + '\'' +
                ", price=" + price +
                '}';
    }
}
