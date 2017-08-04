package io.gopulu.rateLimiter;

import io.gopulu.rateLimiter.cache.CacheStore;
import io.gopulu.rateLimiter.cache.LRUCache;
import io.gopulu.rateLimiter.controller.HotelRestController;
import io.gopulu.rateLimiter.domain.ApiKey;
import io.gopulu.rateLimiter.domain.Hotel;
import io.gopulu.rateLimiter.domain.ICSVParser;
import io.gopulu.rateLimiter.domain.RateLimitTracker;
import io.gopulu.rateLimiter.service.IApiKeyService;
import io.gopulu.rateLimiter.service.IAsyncApiKeyService;
import io.gopulu.rateLimiter.service.IHotelService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.*;

@RunWith(SpringRunner.class)
@WebMvcTest(value = HotelRestController.class, secure = false)
public class RateLimiterApplicationTests {

	private static final Logger log = LoggerFactory.getLogger(RateLimiterApplicationTests.class);
	@Rule
	public TestName testName = new TestName();
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private IHotelService hotelService;

	@MockBean
	private IApiKeyService apiKeyService;

	@MockBean
	private IAsyncApiKeyService asyncApiKeyService;

	@MockBean
	private CacheStore cacheStore;

	@MockBean
	private ICSVParser csvParser;

	LRUCache<String, ApiKey> keyCache = new LRUCache<String, ApiKey>();
	List<Hotel> hotels = new ArrayList<>();
	@Before
	public void logTestStart() {

		log.info("Starting: {}", testName.getMethodName());


		keyCache.put("KEY-01",createApiKey());

		Mockito.when(cacheStore.getCacheInstance(Mockito.anyString())).thenReturn(keyCache);

		Hotel hotel = new Hotel.HotelBuilder("Bangkok","1").room("Deluxe").price(5000).buildHotel();

		hotels.add(hotel);



	}

	@Test
	public void testForServerHealth() throws Exception {
		//Given
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
				"/").accept(
				MediaType.APPLICATION_JSON);

		//When
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		//Then
		Assert.assertEquals("API is up and running...",result.getResponse().getContentAsString());

	}

	//test concurrency

	@Test
	public void testForTwoConcurrentRequestsBySameApiKey() throws Exception {

		//Given

		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/hotels/1")
				.header("AUTHORIZED-API-KEY","abcXYZkeyAPI-0")
				//.requestAttr("AUTHORIZED-API-KEY","abcXYZkeyAPI-0")
				.accept(
						MediaType.APPLICATION_JSON);

		Mockito.when(hotelService.hotelsByCityId(Mockito.anyString())).thenReturn(hotels);

		Mockito.when(apiKeyService.isApiKeyValid(Mockito.anyString())).thenReturn(true);
		Mockito.when(apiKeyService.isApiKeyActive(Mockito.anyString())).thenReturn(true);

		//When : two concurrent requests within given access window by same api key

		mockMvc.perform(requestBuilder).andReturn();
		MvcResult result1 = mockMvc.perform(requestBuilder).andReturn();

		//Then : api key should be suspended for 5 minutes

		Assert.assertEquals(401,result1.getResponse().getStatus());
		Assert.assertEquals("API Key is suspended for sometime",result1.getResponse().getErrorMessage());

	}

	@Test
	public void testGetHotelDataForSingleRequest() throws Exception {

		//Given

		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/hotels/1")
				.header("AUTHORIZED-API-KEY","abcXYZkeyAPI-0")
				.accept(
						MediaType.APPLICATION_JSON);

		Mockito.when(hotelService.hotelsByCityId(Mockito.anyString())).thenReturn(hotels);

		Mockito.when(apiKeyService.isApiKeyValid(Mockito.anyString())).thenReturn(true);
		Mockito.when(apiKeyService.isApiKeyActive(Mockito.anyString())).thenReturn(true);
		Mockito.when(apiKeyService.canApiKeyBeNotSuspended(Mockito.anyString())).thenReturn(true);


		//When :

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		//Then :

		Assert.assertEquals(200,result.getResponse().getStatus());
		Assert.assertEquals("[{\"city\":\"Bangkok\",\"id\":\"1\",\"room\":\"Deluxe\",\"price\":5000.0}]",result.getResponse().getContentAsString());



	}

	private ApiKey createApiKey() {
		Map<String, RateLimitTracker> trackerMap = new HashMap<>();
		RateLimitTracker tracker = new RateLimitTracker(1, 1000);
		ApiKey key = new ApiKey("KEY-01", 0, tracker, true, 1);
		return key;
	}

}
