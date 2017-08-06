package io.gopulu.rateLimiter.async;


import io.gopulu.rateLimiter.cache.CacheStore;
import io.gopulu.rateLimiter.cache.LRUCache;
import io.gopulu.rateLimiter.domain.Hotel;
import io.gopulu.rateLimiter.service.HotelService;
import org.junit.*;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class HotelServiceTest {
    private static final Logger log = LoggerFactory.getLogger(HotelServiceTest.class);
    @Rule
    public TestName testName = new TestName();


    @Mock
    CacheStore cacheStore;

    @InjectMocks
    HotelService hotelService;


    @Before
    public void logTestStart() {

        log.info("Starting: {}", testName.getMethodName());

        hotelService.createExecutorService(4);

        LRUCache<String, CopyOnWriteArrayList<Hotel>> hotelCache = new LRUCache<String, CopyOnWriteArrayList<Hotel>>();
        hotelCache.put("1", createHotels());
        when(cacheStore.getCacheInstance(Mockito.anyString())).thenReturn(hotelCache);

    }

    @Test
    public void testAscOrderOfPrice() throws ExecutionException, InterruptedException {

        CopyOnWriteArrayList<Hotel> ascOrderPriceOfhotels = hotelService.sortAscHotelsOfCityByPrice("1").get();

        double price1 = ascOrderPriceOfhotels.get(0).getPrice();
        double price2 = ascOrderPriceOfhotels.get(1).getPrice();
        double price3 = ascOrderPriceOfhotels.get(2).getPrice();

        Assert.assertEquals(400.0, price1, 0);
        Assert.assertEquals(5000.0, price2, 0);
        Assert.assertEquals(25000.0, price3, 0);


    }

    @Test
    public void testDescOrderOfPrice() throws ExecutionException, InterruptedException {


        CopyOnWriteArrayList<Hotel> descOrderPriceOfhotels = hotelService.sortDescHotelsOfCityByPrice("1").get();

        double price1 = descOrderPriceOfhotels.get(0).getPrice();
        double price2 = descOrderPriceOfhotels.get(1).getPrice();
        double price3 = descOrderPriceOfhotels.get(2).getPrice();

        Assert.assertEquals(25000.0, price1, 0);
        Assert.assertEquals(5000.0, price2, 0);
        Assert.assertEquals(400.0, price3, 0);


    }

    private CopyOnWriteArrayList<Hotel> createHotels() {
        CopyOnWriteArrayList<Hotel> hotels = new CopyOnWriteArrayList<>();

        Hotel hotel1 = new Hotel.HotelBuilder("Bangkok", "1").room("Superior").price(5000).buildHotel();
        Hotel hotel2 = new Hotel.HotelBuilder("Bangkok", "1").room("Sweet Suite").price(25000).buildHotel();
        Hotel hotel3 = new Hotel.HotelBuilder("Bangkok", "1").room("Deluxe").price(400).buildHotel();

        hotels.add(hotel1);
        hotels.add(hotel2);
        hotels.add(hotel3);
        return hotels;
    }

    @After
    public void stopPool() throws InterruptedException {
        hotelService.stopPool();
    }
}
