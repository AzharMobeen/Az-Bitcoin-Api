/**
 * 
 */
package com.az.bitcoin.util;

import java.util.Currency;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Azhar Mobeen
 *
 * Jan 18, 2019
 */

@Slf4j
public final class CurrencyUtil {

	private CurrencyUtil() {
	}

	/*
	 * this method will check Currency is valid by using java.util.Currency
	 * */
	public static boolean isValidateCurrencyCode(String currencyCode) {
		try {			
			Currency currency = Currency.getInstance(currencyCode);
			log.debug("\n\tCurrency Code is valid by java.util.Currency");
			log.debug("\n\tCurrency Code: " + currency.getSymbol());
			return currency.getCurrencyCode().equals(currencyCode);
		} catch (Exception e) {
			log.error("Cannot parse the Currency Code, Validation Failed: ");
		}
		return false;
	}
	
	/*
	 * This method will return currency symbol by using java.util.Currency
	 * */
	public static String getCurrencySymbol(String currencyCode) {
		try {
			Currency currency = Currency.getInstance(currencyCode);			
			return currency.getSymbol();
		} catch (Exception e) {
			log.warn("Cannot parse the Currency Code, Validation Failed: ", e);
		}
		return "";
	}
}
