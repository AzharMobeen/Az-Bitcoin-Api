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
public enum CurrencyUtil {

	INSTANCE;

	public boolean isValidateCurrencyCode(String currencyCode) {
		try {
			Currency currency = Currency.getInstance(currencyCode);			
			log.debug("Currency Code: " + currency.getSymbol());			
			return currency.getCurrencyCode().equals(currencyCode);
		} catch (Exception e) {
			log.warn("Cannot parse the Currency Code, Validation Failed: ", e);
		}
		return false;
	}
	
	public String getCurrencySymbol(String currencyCode) {
		try {
			Currency currency = Currency.getInstance(currencyCode);			
			log.debug("Currency Code: " + currency.getSymbol());			
			return currency.getSymbol();
		} catch (Exception e) {
			log.warn("Cannot parse the Currency Code, Validation Failed: ", e);
		}
		return "";
	}
}
