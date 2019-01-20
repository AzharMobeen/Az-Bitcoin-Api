package com.az.bitcoin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;

import com.az.bitcoin.domain.CurrencyRate;
import com.az.bitcoin.service.BitCoinService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BitCoinServiceTests {

	@Autowired
	private BitCoinService bitCoinService;

	/*
	 * I'm passing correct currency code so test will be successful
	 */
	@Test
	public void fetchCurrentBitCoinRateSuccessTest() {
		String currency = "AED";
		CurrencyRate currencyRate = bitCoinService.fetchCurrentBitCoinRate(currency);
		assertNotNull("Should return current Rate", currencyRate.getCurrentRate());
		assertNotEquals("Should not equals, because it's valid currency", true, currencyRate.isCurrencyNotFound());
	}

	/*
	 * I'm passing wrong currency so I will get Currency not found = true and
	 * Current rate must be null
	 */
	@Test
	public void fetchCurrentBitCoinRateFailedTest() {
		String currency = "AEddD";
		CurrencyRate currencyRate = bitCoinService.fetchCurrentBitCoinRate(currency);
		assertNull("Should return null current Rate", currencyRate.getCurrentRate());
		assertEquals("Should equals, because it's not valid currency", true, currencyRate.isCurrencyNotFound());
	}

	/*
	 * for checking lowest and highest currency we need to call this url:
	 * https://api.coindesk.com/v1/bpi/historical/close.json?currency= and I'm
	 * passing wrong currency code so http 404 error will be expected.
	 */
	@Test(expected = HttpClientErrorException.class)
	public void fetchLowestAndHighestBitCoinRateFailedTest() {
		String currency = "AEddD";
		bitCoinService.fetchLowestAndHighestBitCoinRate(currency);
	}

	/*
	 * I'm passing correct currency code so it should return max and min rate
	 */	
	@Test
	public void fetchLowestAndHighestBitCoinRateSuccessTest() {
		String currency = "AED";
		CurrencyRate currencyRate = bitCoinService.fetchLowestAndHighestBitCoinRate(currency);
		assertNotNull("Should return Max Rate", currencyRate.getMaxRate());
		assertNotNull("Should return Min Rate", currencyRate.getMinRate());
		assertNotEquals("Should not equals, because it's valid currency", true, currencyRate.isCurrencyNotFound());
	}

}
