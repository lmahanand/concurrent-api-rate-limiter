package io.gopulu.rateLimiter.domain;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CSVParser implements ICSVParser {

    //@Value("${mongodb.url:127.0.0.1}")
    @Value("apikeys.csv")
    private String keyResource;

    @Value("2000")
    private long accessWindow;

    //@Value("1")
    @Value("${GLOBAL-RATE-LIMIT}")
    private int globalRateLimit;

    @Autowired
    private ICsvToHotelMapper csvToHotelMapper;

    @Override
    public List<ApiKey> parseLinesToApiKey() {
        ClassLoader classLoader = getClass().getClassLoader();

        Path path = null;
        try {
            path = Paths.get(classLoader.getResource(keyResource).toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        List<ApiKey> apiKeys = Arrays.asList();

        try (BufferedReader br = Files.newBufferedReader(path)) {


            br.readLine();
            apiKeys = br.lines().map(line -> createApiKey(Arrays.asList(line.split(",")))).collect(Collectors.toList());


        } catch (IOException e) {
            e.printStackTrace();
        }

        return apiKeys;
    }

    @Override
    public List<Hotel> parseLinesToHotels(final String resource) {

        ClassLoader classLoader = getClass().getClassLoader();

        Path path = null;
        try {
            path = Paths.get(classLoader.getResource(resource).toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        List<Hotel> hotels = Arrays.asList();

        try (BufferedReader br = Files.newBufferedReader(path)) {

            br.readLine();

            hotels = br.lines().map(line -> csvToHotelMapper.mapCSVToHotel(Arrays.asList(line.split(","))))
                    .collect(Collectors.toList());

        } catch (IOException e) {
            e.printStackTrace();
        }

        return hotels;

    }

    private ApiKey createApiKey(final List<String> key) {

        if (key.size() > 1) {
            RateLimitTracker limitTracker = new RateLimitTracker(Integer.valueOf(key.get(1)),accessWindow);
            return new ApiKey(key.get(0), 0, limitTracker, true, Integer.valueOf(key.get(1)));
        }
        RateLimitTracker limitTracker = new RateLimitTracker(globalRateLimit,accessWindow);
        return new ApiKey(key.get(0), 0, limitTracker, true, globalRateLimit);
    }
}
