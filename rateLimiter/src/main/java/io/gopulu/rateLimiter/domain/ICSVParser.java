package io.gopulu.rateLimiter.domain;

import io.gopulu.rateLimiter.domain.Hotel;

import java.util.List;

public interface ICSVParser {
    public List<Hotel> parseLinesToHotels(final String resource);
    public List<ApiKey> parseLinesToApiKey();
}
