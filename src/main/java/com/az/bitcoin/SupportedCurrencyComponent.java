/**
 * 
 */
package com.az.bitcoin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import com.az.bitcoin.domain.SupportedCurrency;
import com.az.bitcoin.service.BitCoinService;

/**
 * @author Azhar Mobeen
 *
 * Jan 18, 2019
 */
@Component
public class SupportedCurrencyComponent implements CommandLineRunner{

	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private BitCoinService bitCoinService;
	
	@Value("${supportedCurrencyUrl}")
	private String SUPPORTED_CURRENCY_URL;
	
	/*
	 * It will run just after SpringBoot application and 
	 * I will fetch supported currency list by CoinDesk Api once
	 * and I will use this data for all user requests
	 * */
	public void run(String... args){
		List<SupportedCurrency> supportedCurrencyList = null;
		ResponseEntity<List<SupportedCurrency>> currencyResponse = 
				restTemplate.exchange(SUPPORTED_CURRENCY_URL,HttpMethod.GET,null,new ParameterizedTypeReference<List<SupportedCurrency>>() {
		} );
		supportedCurrencyList = currencyResponse.getBody();
		if(!CollectionUtils.isEmpty(supportedCurrencyList))
			bitCoinService.setSupportedCurrencyList(supportedCurrencyList);
		
	}

}
