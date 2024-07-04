package com.demo.urlshortner.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.urlshortner.constants.ServiceConstants;
import com.demo.urlshortner.request.ShortUrlRequest;
import com.demo.urlshortner.request.UpdateDestinationUrlRequest;
import com.demo.urlshortner.request.UpdateExpireByShortUrl;
import com.demo.urlshortner.response.ResponseUtility;
import com.demo.urlshortner.service.UrlServiceInterface;

@RestController
@RequestMapping("/api/url")
public class UrlController {

	private static final Logger logger = LoggerFactory.getLogger(UrlController.class);

	private final UrlServiceInterface urlServiceInterface;
	private final ResponseUtility responseUtility;

	public UrlController(UrlServiceInterface urlServiceInterface, ResponseUtility responseUtility) {
		this.urlServiceInterface = urlServiceInterface;
		this.responseUtility = responseUtility;
	}

	/**
	 * Endpoint to shorten a URL.
	 *
	 * @param shortUrlRequest Request body containing the destination URL.
	 * @return ResponseEntity with appropriate status and message.
	 */
	@PostMapping("/save")
	public ResponseEntity<Object> shortenUrl(@RequestBody ShortUrlRequest shortUrlRequest) {
		try {
			logger.info("ShortenUrl controller method started");
			return urlServiceInterface.shortenUrl(shortUrlRequest);
		} catch (Exception e) {
			logger.error("Error in shortenUrl controller method", e);
			return responseUtility.createResponse(ServiceConstants.HTTP_STATUS_INTERNAL_SERVER_ERROR,
					ServiceConstants.RESPONSE_INTERNAL_SERVER_ERROR, "Error while processing request", null);
		}
	}

	/**
	 * Endpoint to get DestinationURL from shortenURL.
	 *
	 * @param shortUrl Short URL to retrieve the destination URL.
	 * @return ResponseEntity with appropriate status and message.
	 */
	@GetMapping("/destination/{shortUrl}")
	public ResponseEntity<Object> getDestinationURLByShortURL(@PathVariable("shortUrl") String shortUrl) {
		try {
			logger.info("GetDestinationURLByShortURL controller method started");
			return urlServiceInterface.getDestinationURLByShortURL(shortUrl);
		} catch (Exception e) {
			logger.error("Error in GetDestinationURLByShortURL controller method", e);
			return responseUtility.createResponse(ServiceConstants.HTTP_STATUS_INTERNAL_SERVER_ERROR,
					ServiceConstants.RESPONSE_INTERNAL_SERVER_ERROR, "Error while processing request", null);
		}
	}

	/**
	 * Endpoint to update destination URL by short URL.
	 *
	 * @param updateDestinationUrlRequest Request body containing the new
	 *                                    destination URL and short URL.
	 * @return ResponseEntity with appropriate status and message.
	 */
	@PostMapping("/update")
	public ResponseEntity<Object> updateDestinationUrlByShortUrl(
			@RequestBody UpdateDestinationUrlRequest updateDestinationUrlRequest) {
		try {
			logger.info("UpdateDestinationUrlByShortUrl controller method started");
			return urlServiceInterface.updateDestinationUrlByShortUrl(updateDestinationUrlRequest);
		} catch (Exception e) {
			logger.error("Error in UpdateDestinationUrlByShortUrl controller method", e);
			return responseUtility.createResponse(ServiceConstants.HTTP_STATUS_INTERNAL_SERVER_ERROR,
					ServiceConstants.RESPONSE_INTERNAL_SERVER_ERROR, "Error while processing request", null);
		}
	}

	/**
	 * Endpoint to update expire days URL by short URL.
	 *
	 * @param updateExpireByShortUrl Request body containing the new
	 *                                    expireDays and short URL.
	 * @return ResponseEntity with appropriate status and message.
	 */
	@PostMapping("/update/expiredays")
	public ResponseEntity<Object> updateExpireDaysByShortUrl(@RequestBody UpdateExpireByShortUrl updateExpireByShortUrl)
			throws Exception {
		try {
			logger.info("updateExpireByShortUrl controller method started");
			return urlServiceInterface.updateExpireDaysByShortUrl(updateExpireByShortUrl);
		} catch (Exception e) {
			logger.error("Error in UpdateDestinationUrlByShortUrl controller method", e);
			return responseUtility.createResponse(ServiceConstants.HTTP_STATUS_INTERNAL_SERVER_ERROR,
					ServiceConstants.RESPONSE_INTERNAL_SERVER_ERROR, "Error while processing request", null);
		}
	}
}
