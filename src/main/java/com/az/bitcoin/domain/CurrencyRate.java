/**
 * 
 */
package com.az.bitcoin.domain;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.Data;

/**
 * @author Azhar Mobeen
 *
 * Jan 19, 2019
 */

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CurrencyRate implements Serializable{

	private String code;
		
	@JsonProperty(value="rate")	
	private String rate;
	
	private String description;
	
	@JsonProperty(value="rate_float")
	@JsonDeserialize(as=BigDecimal.class)
	private BigDecimal floatRate;
}
