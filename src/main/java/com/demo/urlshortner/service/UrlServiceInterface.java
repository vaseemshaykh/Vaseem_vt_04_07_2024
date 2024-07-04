package com.demo.urlshortner.service;

import org.springframework.http.ResponseEntity;

import com.demo.urlshortner.request.ShortUrlRequest;
import com.demo.urlshortner.request.UpdateDestinationUrlRequest;
import com.demo.urlshortner.request.UpdateExpireByShortUrl;

public interface UrlServiceInterface {

	ResponseEntity<Object> shortenUrl(ShortUrlRequest shortUrlRequest) throws Exception;

	ResponseEntity<Object> getDestinationURLByShortURL(String shortUrl) throws Exception;

	ResponseEntity<Object> updateDestinationUrlByShortUrl(UpdateDestinationUrlRequest updateDestinationUrlRequest)
			throws Exception;

	ResponseEntity<Object> updateExpireDaysByShortUrl(UpdateExpireByShortUrl updateExpireByShortUrl) throws Exception;

}
