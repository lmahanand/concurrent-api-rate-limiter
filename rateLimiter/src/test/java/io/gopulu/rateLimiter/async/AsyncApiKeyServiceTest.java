package io.gopulu.rateLimiter.async;

import io.gopulu.rateLimiter.domain.ApiKey;
import io.gopulu.rateLimiter.domain.RateLimitTracker;
import io.gopulu.rateLimiter.service.AsyncApiKeyService;
import org.junit.*;
import org.junit.rules.TestName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Test cases to verify asynchronous response of the apis
 */

public class AsyncApiKeyServiceTest {
    private static final Logger log = LoggerFactory.getLogger(AsyncApiKeyServiceTest.class);

    AsyncApiKeyService asyncApiKeyService = new AsyncApiKeyService();

    @Rule
    public TestName testName = new TestName();

    @Before
    public void logTestStart() {
        asyncApiKeyService.createExecutorService(10);
        log.info("Starting: {}", testName.getMethodName());
    }

    @After
    public void stopPool() throws InterruptedException {
        asyncApiKeyService.stopPool();
    }

    @Test
    public void testEnableApiKeyOfSuspendedApiKey() {
        //given

        ApiKey apiKey = createApiKey();
        apiKey.setActive(false);

        //when
        asyncApiKeyService.enableApiKey(apiKey);

        //then
        Assert.assertEquals(true, apiKey.isActive());

    }

    @Test
    public void testSuspendApiKey() {
        //given
        ApiKey apiKey = createApiKey();
        apiKey.setActive(true);

        //when
        asyncApiKeyService.suspendApiKey(apiKey);
        try {
            //System.out.println(Thread.currentThread().getName());
            Thread.currentThread().sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //then
        Assert.assertEquals(false, apiKey.isActive());

    }

    @Test
    public void testApiKeyShouldNotBeSuspendedAfterVerifyApiAccessRate(){
        //given
        ApiKey apiKey = createApiKey();

        //when
        boolean apiKeyShouldNotBeSuspended = asyncApiKeyService.verifyApiAccessRate(apiKey.getLimitTracker());

        //then

        Assert.assertEquals(true,apiKeyShouldNotBeSuspended);
    }

    private ApiKey createApiKey() {
        Map<String, RateLimitTracker> trackerMap = new HashMap<>();
        RateLimitTracker tracker = new RateLimitTracker(1, 1000);
        ApiKey key = new ApiKey("KEY-01", 0, tracker, true, 1);
        return key;
    }

}
