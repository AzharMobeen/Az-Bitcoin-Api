package com.az.bitcoin;

import java.util.Collections;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.az.bitcoin.service.BitCoinService;

@SpringBootApplication
public class AzApplication {
	
	public static void main(String[] args) {

		ConfigurableApplicationContext configurableApplicationContext = SpringApplication.run(AzApplication.class,args);
		BitCoinService bitCoinServiceImpl = configurableApplicationContext.getBean(BitCoinService.class);
		bitCoinServiceImpl.askUserToEnterCurrency();
		configurableApplicationContext.close();
	}

	@Bean
	public RestTemplate getRestTemplate(RestTemplateBuilder builder) {
		RestTemplate restTemplate = new RestTemplate();
	    MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();	    
	    converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
	    restTemplate.getMessageConverters().add(converter);
	    return restTemplate;
	}	
}
