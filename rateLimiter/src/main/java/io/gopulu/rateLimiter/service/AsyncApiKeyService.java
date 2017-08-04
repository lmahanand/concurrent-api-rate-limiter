package io.gopulu.rateLimiter.service;

import io.gopulu.rateLimiter.domain.ApiKey;
import io.gopulu.rateLimiter.domain.RateLimitTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.*;

/**
 * AsyncApiKeyService class provides Asychronous services
 * against each API Key
 */

@Service
public class AsyncApiKeyService implements IAsyncApiKeyService {


    private final Logger LOGGER = LoggerFactory.getLogger(AsyncApiKeyService.class);

    @Value("${THREADS}")
    private int nThreads;

    protected ExecutorService executorService;

    @PostConstruct
    public void init() {
        executorService = Executors.newFixedThreadPool(nThreads);
    }

    @Override
    public void suspendApiKey(ApiKey apiKey) {
        CompletableFuture<Void> trackerFlag =
                CompletableFuture.runAsync(() -> {
                    apiKey.setLastAccessTime(System.currentTimeMillis());
                    apiKey.setActive(false);
                    LOGGER.info("API KEY is Deactivated");

                },executorService);

    }


    private boolean enable(ApiKey apiKey){
        apiKey.setLastAccessTime(0);
        apiKey.setActive(true);
        LOGGER.info("API KEY is Activated");
        return true;
    }
    @Override
    public boolean enableApiKey(ApiKey apiKey) {
        final CompletableFuture<Boolean> isActiveCompletableFuture = CompletableFuture.supplyAsync(() -> enable(apiKey));
        boolean isActivated = false;
        try {
            isActivated = isActiveCompletableFuture.get();
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage());

        } catch (ExecutionException e) {
            LOGGER.error(e.getMessage());
        }
        return isActivated;
    }


    @Override
    public boolean verifyApiAccessRate(RateLimitTracker rateLimitTracker) {
        final CompletableFuture<Boolean> trackerFlag = CompletableFuture.supplyAsync(() ->
                        rateLimitTracker.isAccessWithinRateLimit(),
                executorService);

        final CompletableFuture<Boolean> recovered = trackerFlag
                .handle((result, throwable) -> {
                    if (throwable != null) {

                        throw new RuntimeException();
                    } else {
                        return result;
                    }
                });
        final boolean flag;

        try {
            flag = recovered.get();
            return flag;
        } catch (InterruptedException e) {
            LOGGER.error("Interrupted exception occurred: " + e);

        } catch (ExecutionException e) {
            LOGGER.error("Execution exception occurred: " + e);
        }

        return false;
    }

    @Override
    public void stopPool() throws InterruptedException {
        executorService.shutdown();
        executorService.awaitTermination(10, TimeUnit.SECONDS);
    }

    public void createExecutorService(int nThreads){
        executorService = Executors.newFixedThreadPool(nThreads);
    }
}
