/**
 * 
 */
package com.az.bitcoin.domain;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

/**
 * @author Azhar Mobeen
 *
 * Jan 17, 2019
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SupportedCurrency implements Serializable{

	private String currency;
	private String country;
	
}
