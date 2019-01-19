/**
 * 
 */
package com.az.bitcoin.util;

import java.time.LocalDate;

/**
 * @author Azhar Mobeen
 *
 * Jan 19, 2019
 */

public enum DateUtil {

	INSTANCE;		
	
	/*
	 * As per requirements end date should be current date
	 * Default format YYYY-MM-DD Which CoinDesk Api required
	 * */
	public String getEndDate() {
		LocalDate endDate = LocalDate.now();				
		return endDate.toString();
				
	}
	
	/*
	 * As per requirements start date should be before 30 days from now
	 * Default format YYYY-MM-DD Which CoinDesk Api required
	 * */
	public String getStartDate() {
		LocalDate startDate = LocalDate.now();
		startDate = startDate.minusDays(30);
		return startDate.toString();
	}		
}
