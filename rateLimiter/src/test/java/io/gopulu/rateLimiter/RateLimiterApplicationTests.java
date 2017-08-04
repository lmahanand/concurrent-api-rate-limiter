package io.gopulu.rateLimiter;

import io.gopulu.rateLimiter.cache.CacheStore;
import io.gopulu.rateLimiter.controller.HotelRestController;
import io.gopulu.rateLimiter.domain.ICSVParser;
import io.gopulu.rateLimiter.service.IApiKeyService;
import io.gopulu.rateLimiter.service.IAsyncApiKeyService;
import io.gopulu.rateLimiter.service.IHotelService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@RunWith(SpringRunner.class)
//@SpringBootTest
@WebMvcTest(value = HotelRestController.class, secure = false)
public class RateLimiterApplicationTests {
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

}
