package com.demo.urlshortner.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import com.demo.urlshortner.controller.UrlController;
import com.demo.urlshortner.model.ResponseModel;
import com.demo.urlshortner.response.ResponseUtility;
import com.demo.urlshortner.service.UrlServiceInterface;

@Configuration
@EnableCaching
//@EnableAsync
public class BeanConfigurations {

	@Bean
	public ResponseUtility responseUtility() {
		return new ResponseUtility();
	}

	@Bean
	public ResponseModel responseModel() {
		return new ResponseModel();
	}

	@Bean
	public UrlController urlController(UrlServiceInterface urlServiceInterface, ResponseUtility responseUtility) {
		return new UrlController(urlServiceInterface, responseUtility);
	}

	@Bean
	public CacheManager cacheManager() {
		return new ConcurrentMapCacheManager("urlsCache"); 
	}
}
