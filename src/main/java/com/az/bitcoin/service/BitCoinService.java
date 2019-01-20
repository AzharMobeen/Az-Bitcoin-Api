/**
 * 
 */
package com.az.bitcoin.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

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
public class BitCoinService{

	@Autowired
	private RestTemplate restTemplate;
	boolean currencyFlag = false;
	private List<SupportedCurrency> supportedCurrencyList;
	@Value("${supportedCurrencyUrl}")
	private String SUPPORTED_CURRENCY_URL;
	@Value("${currentRateUrl}")
	private String CURRENT_RATE_URL;
	@Value("${historicalRateUrl}")
	private String HISTORICAL_RATE_URL;
	private CurrencyRate currencyRate;
	
	/*	
	 * First I'm checking currency is valid or not, 
	 * then supported by CoinDesk or not and at the end fetch Current BitCoin rate
	 * with respect to entered currency. 
	 */	
	public CurrencyRate fetchCurrentBitCoinRate(String currency) {
		currencyRate = new CurrencyRate();
		if (checkSupportedCurrency(currency)) {			
			log.debug("\n\tCurrency is supported by CoinDesk API");
			ResponseEntity<String> response = restTemplate.getForEntity(CURRENT_RATE_URL + currency + ".json",
					String.class);
			ObjectMapper mapper = new ObjectMapper();
			JsonNode root;
			try {
				root = mapper.readTree(response.getBody());								
				JsonNode bpi = root.path("bpi");
				JsonNode requiredCurrency = bpi.get(currency);
				currencyRate = mapper.convertValue(requiredCurrency, CurrencyRate.class);
				if (currencyRate != null) {
					currencyRate = fetchLowestAndHighestBitCoinRate(currency);
				}
			} catch (IOException e) {
				log.error("IOException occurs: "+e);
			}			
		}else
			currencyRate.setCurrencyNotFound(true);
		return currencyRate;
	}

	/*
	 * This method is used by this.checkSupportedCurrency
	 * First I'm checking User entered currency is valid currency or not then I'm checking
	 * it's supported by CoinDesk APi or not Please check CurrencyUtil
	 */
	private boolean checkSupportedCurrency(String currency) {

		if (CurrencyUtil.isValidateCurrencyCode(currency)) {
			log.debug("\nCurrency code is valid");
			if (CollectionUtils.isEmpty(supportedCurrencyList)) {
				ResponseEntity<List<SupportedCurrency>> currencyResponse = restTemplate.exchange(SUPPORTED_CURRENCY_URL,
						HttpMethod.GET, null, new ParameterizedTypeReference<List<SupportedCurrency>>() {
						});
				supportedCurrencyList = currencyResponse.getBody();
			}
			supportedCurrencyList.forEach(supportedCurrency -> {
				if (currency.equalsIgnoreCase(supportedCurrency.getCurrency()))
					currencyFlag = true;
			});
		} else {
			log.info("\nCurrency code is invalid <<< " + currency + " >>>");
			currencyFlag = false;
		}
		return currencyFlag;
	}

	/*
	 * In this method I will fetch lowest and highest Rate with respect to entered
	 * currency
	 */
	public CurrencyRate fetchLowestAndHighestBitCoinRate(String currency) {		
		if(currencyRate==null)
			currencyRate = new CurrencyRate();
		String startDate = DateUtil.getStartDate();
		String endDate = DateUtil.getEndDate();
		currencyRate.setStartDate(startDate);
		currencyRate.setEndDate(endDate);
		String url = HISTORICAL_RATE_URL + currency + "&start=" + startDate + "&end=" + endDate;
		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
		ObjectMapper mapper = new ObjectMapper();
		JsonNode root;
		try {
			
			root = mapper.readTree(response.getBody());
			JsonNode bpi = root.path("bpi");
			@SuppressWarnings("unchecked")
			Map<String, Double> result = mapper.convertValue(bpi, Map.class);
			if (result != null) {				
				Double minRate = result.entrySet().stream().min(Map.Entry.comparingByValue()).get().getValue();
				String minRateDate = result.entrySet().stream().min(Map.Entry.comparingByValue()).get().getKey();
				currencyRate.setMinRate(minRate);
				currencyRate.setMinRateDate(minRateDate);
				Double maxRate = result.entrySet().stream().max(Map.Entry.comparingByValue()).get().getValue();
				String maxRateDate = result.entrySet().stream().max(Map.Entry.comparingByValue()).get().getKey();
				currencyRate.setMaxRate(maxRate);
				currencyRate.setMaxRateDate(maxRateDate);
				currencyRate.setCurrencyNotFound(false);
			}
		} catch (IOException e) {
			log.error("Exception occurs: " + e);			
		}
		return currencyRate;
	}

	/*
	 * This method will ask user for to enter currency and then it will display required details.
	 * this method will execute infinite time
	 * */
	public void askUserToEnterCurrency() {
		String repeat = "y";
	    	do {
				@SuppressWarnings("resource")
				Scanner scanner = new Scanner(System.in);
				System.out.print("\nPlease enter the currency code e.g USD, EUR, GBP: ");			
				String currency = scanner.next();				
				this.fetchCurrentBitCoinRate(currency.toUpperCase());
				this.displayResult(currencyRate);
				System.out.print("\nTo inquire another currency type Y : ");
				repeat = scanner.next();
		}while(repeat.equalsIgnoreCase("y"));
	}
	
	private void displayResult(CurrencyRate currencyRate) {
		if(!currencyRate.isCurrencyNotFound()) {
			String currencySymbol = CurrencyUtil.getCurrencySymbol(currencyRate.getCode());				
			System.out.println("\n\n\tCurrent rate is :::: " + currencySymbol + " "  + currencyRate.getCurrentRate());
			System.out.println(
					"\n\tThe lowest Bitcoin rate in the last 30 days is :::: "+ currencyRate.getMinRateDate() +" "+ currencySymbol + " " + currencyRate.getMinRate());
			System.out.println(
					"\n\tThe highest Bitcoin rate in the last 30 days is :::: "+ currencyRate.getMaxRateDate() +" "+ currencySymbol + " " + currencyRate.getMaxRate());
		}
	}
}