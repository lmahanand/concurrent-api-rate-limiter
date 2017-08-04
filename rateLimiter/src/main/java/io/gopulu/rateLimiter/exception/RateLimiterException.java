package io.gopulu.rateLimiter.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class RateLimiterException {

    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Hotels Not Found for given city id.")
    public static class HotelsNotFoundException extends Exception {
        public HotelsNotFoundException(String cityId) {
            super(String.format("Hotels not found for city id: %s", cityId));
        }
    }

    @ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "API Key is not authorized to api access")
    public static class InvalidAPIKeyException extends Exception {
        public InvalidAPIKeyException(String apiKey) {
            super(String.format("Invalid Api key: %s", apiKey));
        }
    }

    @ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "API Key is suspended for sometime")
    public static class SuspendedAPIKeyException extends Exception {
        public SuspendedAPIKeyException(String apiKey) {
            super(String.format("API Key is suspended for sometime: %s", apiKey));
        }
    }

}
