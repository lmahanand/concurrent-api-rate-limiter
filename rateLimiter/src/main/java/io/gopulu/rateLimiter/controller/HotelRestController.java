package io.gopulu.rateLimiter.controller;

import io.gopulu.rateLimiter.domain.Hotel;
import io.gopulu.rateLimiter.service.IApiKeyService;
import io.gopulu.rateLimiter.service.IHotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static io.gopulu.rateLimiter.exception.RateLimiterException.*;

@RestController

public class HotelRestController {
    @Autowired
    private IHotelService hotelService;

    @Autowired
    private IApiKeyService apiKeyService;

    @GetMapping(value = "/")
    public String index() {
        return "API is up and running...";
    }

    @GetMapping(value = "/hotels/{id}")
    public List<Hotel> getHotel(@RequestHeader(value = "AUTHORIZED-API-KEY", required = true) String apiKey, @PathVariable final String id)
            throws HotelsNotFoundException, InvalidAPIKeyException, SuspendedAPIKeyException {
        validateApiKey(apiKey);
        List<Hotel> hotels = hotelService.hotelsByCityId(id);

        if (null == hotels || hotels.size() < 1) {
            throw new HotelsNotFoundException(id);
        }

        return hotels;
    }

    @GetMapping(value = "/hotels/{id}/price/sort/asc")
    public List<Hotel> sortHotelsOfCityByAscPrice(@RequestHeader(value = "AUTHORIZED-API-KEY", required = true) String apiKey, @PathVariable final String id)
            throws HotelsNotFoundException, InvalidAPIKeyException, SuspendedAPIKeyException {
        validateApiKey(apiKey);
        List<Hotel> hotels = hotelService.sortAscHotelsOfCityByPrice(id);

        if (null == hotels || hotels.size() < 1) {
            throw new HotelsNotFoundException(id);
        }

        return hotels;
    }

    @GetMapping(value = "/hotels/{id}/price/sort/desc")
    public List<Hotel> sortHotelsOfCityByDescPrice(@RequestHeader(value = "AUTHORIZED-API-KEY", required = true) String apiKey, @PathVariable final String id)
            throws HotelsNotFoundException, InvalidAPIKeyException, SuspendedAPIKeyException {
        validateApiKey(apiKey);
        List<Hotel> hotels = hotelService.sortDescHotelsOfCityByPrice(id);

        if (null == hotels || hotels.size() < 1) {
            throw new HotelsNotFoundException(id);
        }

        return hotels;
    }

    private void validateApiKey(final String apiKey) throws InvalidAPIKeyException, SuspendedAPIKeyException {

        if (!apiKeyService.isApiKeyValid(apiKey)) {
            throw new InvalidAPIKeyException(apiKey);
        }

        boolean isKeyActive = apiKeyService.isApiKeyActive(apiKey);


        if (!isKeyActive) {
            throw new SuspendedAPIKeyException(apiKey);
        }

        if (!apiKeyService.canApiKeyBeNotSuspended(apiKey)) {
            apiKeyService.suspendApiKey(apiKey);
            throw new SuspendedAPIKeyException(apiKey);
        }
    }
}
