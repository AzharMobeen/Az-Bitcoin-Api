/**
 * 
 */
package com.az.bitcoin.service.impl;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import com.az.bitcoin.domain.CurrencyRate;
import com.az.bitcoin.domain.SupportedCurrency;
import com.az.bitcoin.service.BitCoinService;
import com.az.bitcoin.util.CurrencyUtil;
import com.az.bitcoin.util.DateUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Azhar Mobeen
 *
 * Jan 17, 2019
 */
@Slf4j
@Service
@Data
public class BitCoinServiceImpl implements BitCoinService{
	
	@Autowired
	private RestTemplate restTemplate;
	boolean currencyFlag = false;
	private List<SupportedCurrency> supportedCurrencyList;
	
	@Value("${supportedCurrencyUrl}")
	private String supportedCurrencyUrl;
	@Value("${currentRateUrl}")
	private String currentRateUrl;
	@Value("${historicalRateUrl}")
	private String historicalRateUrl;
	
	public void displyCurrentBitCoinRate(String currency) {		
		if(checkSupportedCurrency(currency)) {
			log.debug("\n\tCurrency is supported by CoinDesk API");						
			ResponseEntity<String> response= restTemplate.getForEntity(currentRateUrl+currency+".json",String.class);
			ObjectMapper mapper = new ObjectMapper();
			JsonNode root;
			CurrencyRate currencyRate = null;
			try {
				root = mapper.readTree(response.getBody());
				//System.out.println("\n\t"+root);
				JsonNode bpi = root.path("bpi");				
				//System.out.println("\n\t"+bpi);
				JsonNode requiredCurrency = bpi.get(currency);
				currencyRate = mapper.convertValue(requiredCurrency, CurrencyRate.class);
				if(currencyRate!=null) {
					//System.out.println("\n\t"+currencyRate);
					System.out.println("\n\tCurrent rate is :::: "+currencyRate.getFloatRate());
					displayLowestAndHighestBitCoinRate(currency);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			/*ResponseEntity<CurrentRate> response =  restTemplate.getForEntity(currentPriceUrl+currency+".json",CurrentRate.class);			
			if(response!=null)
				System.out.println(response.getBody());*/
			
			
		}

	}

	/*
	 * I will check user enter currency is valid currency code 
	 * then I'm checking it's support by CoinDesk APi or not
	 * Please check CurrencyUtil
	 * */
	private boolean checkSupportedCurrency(String currency) {
		
		if(CurrencyUtil.INSTANCE.isValidateCurrencyCode(currency)) {
			log.debug("Currency code is valid <<< "+currency+" >>>");
			if(CollectionUtils.isEmpty(supportedCurrencyList)) {
				ResponseEntity<List<SupportedCurrency>> currencyResponse = 
						restTemplate.exchange(supportedCurrencyUrl,HttpMethod.GET,null,new ParameterizedTypeReference<List<SupportedCurrency>>() {
				} );
				supportedCurrencyList = currencyResponse.getBody();				
			}		
			supportedCurrencyList.forEach(supportedCurrency->{
				if(currency.equalsIgnoreCase(supportedCurrency.getCurrency()))
					currencyFlag = true;
			});
		}else
			log.debug("Currency code is invalid <<< "+currency+" >>>");
		return currencyFlag;
	}
	
	/*
	 * In this method I will display lowest and highest Rate with respect to entered currency
	 * */
	private void displayLowestAndHighestBitCoinRate(String currency) {
		String startDate = DateUtil.INSTANCE.getStartDate();
		String endDate = DateUtil.INSTANCE.getEndDate();
		String url = historicalRateUrl+currency+"&start="+startDate+"&end="+endDate;
		System.out.println(url);
		ResponseEntity<String> response= restTemplate.getForEntity(url,String.class);
		ObjectMapper mapper = new ObjectMapper();
		JsonNode root;		
		try {
			root = mapper.readTree(response.getBody());			
			System.out.println("\n\t"+root);
			JsonNode bpi = root.path("bpi");				
			System.out.println("\n\t"+bpi);
			@SuppressWarnings("unchecked")
			Map<LocalDate, Double> result = mapper.convertValue(bpi,Map.class);				
			if(result!=null) {
				String currencySymbol = CurrencyUtil.INSTANCE.getCurrencySymbol(currency);
				Double minRate = result.entrySet().stream().min(Map.Entry.comparingByValue()).get().getValue();
				Double maxRate = result.entrySet().stream().max(Map.Entry.comparingByValue()).get().getValue();
				System.out.println("\n\tThe lowest Bitcoin rate in the last 30 days is :::: "+currencySymbol+" "+minRate);				
				System.out.println("\n\tThe highest Bitcoin rate in the last 30 days is :::: "+currencySymbol+" "+maxRate);
			}
		} catch (IOException e) {
			log.error("Exception occurs: "+ e.toString());
		}
	}
}
