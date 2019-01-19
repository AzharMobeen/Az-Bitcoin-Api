package com.az.bitcoin;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.az.bitcoin.service.impl.BitCoinServiceImpl;

@SpringBootApplication
public class AzApplication {

	public static void main(String[] args) {

		ConfigurableApplicationContext configurableApplicationContext = SpringApplication.run(AzApplication.class,
				args);
		BitCoinServiceImpl bitCoinService = configurableApplicationContext.getBean(BitCoinServiceImpl.class);
		Scanner scanner = new Scanner(System.in);
		// prompt for the user for currency
		System.out.print("Please enter currency: ");
		// get their input as a String
		String currency = scanner.nextLine();
		System.out.println(currency);
		bitCoinService.displyCurrentBitCoinRate(currency);
	}

	@Bean
	public RestTemplate getRestTemplate(RestTemplateBuilder builder) {
		RestTemplate restTemplate = new RestTemplate();
	    MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
	    List<MediaType> mediaTypeList = new ArrayList<MediaType>();
	    //mediaTypeList.add(MediaType.TEXT_HTML);
	    mediaTypeList.add(MediaType.ALL);
	    //mediaTypeList.add(new MediaType("application","javascript"));
	    //Collections.singletonList(mediaTypeList);
	    converter.setSupportedMediaTypes(mediaTypeList);
	    restTemplate.getMessageConverters().add(converter);
	    return restTemplate;
	}

}
