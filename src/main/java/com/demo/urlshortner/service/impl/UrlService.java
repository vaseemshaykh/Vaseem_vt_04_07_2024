package com.demo.urlshortner.service.impl;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.demo.urlshortner.constants.ServiceConstants;
import com.demo.urlshortner.model.Urls;
import com.demo.urlshortner.repository.UrlsRepository;
import com.demo.urlshortner.request.ShortUrlRequest;
import com.demo.urlshortner.request.UpdateDestinationUrlRequest;
import com.demo.urlshortner.request.UpdateExpireByShortUrl;
import com.demo.urlshortner.response.ResponseUtility;
import com.demo.urlshortner.service.UrlServiceInterface;

@Service
public class UrlService implements UrlServiceInterface {

    @Value("${url.expire.days.default-value}")
    private int defaultExpiryDays;

    @Value("${base.url.prefix}")
    private String baseUrlPrefix;

    @Value("${shorturl.length.default-value}")
    private int shortUrlLength;

    @Autowired
    private UrlsRepository urlsRepository;

    @Autowired
    private ResponseUtility responseUtility;

    private static final Logger logger = LoggerFactory.getLogger(UrlService.class);

    @Override
    public ResponseEntity<Object> shortenUrl(ShortUrlRequest shortUrlRequest) {
        try {
            logger.info("shortenUrl service method started");

            // Validate request
            if (shortUrlRequest == null || shortUrlRequest.getDestinationURL() == null
                    || shortUrlRequest.getDestinationURL().isEmpty()) {
                return responseUtility.createResponse(ServiceConstants.HTTP_STATUS_BAD_REQUEST,
                        ServiceConstants.RESPONSE_BAD_REQUEST, ServiceConstants.ERROR_DESTINATION_URL_EMPTY, null);
            }

            // Check if destination URL already exists
            Urls existingUrl = urlsRepository.findByDestinationUrl(shortUrlRequest.getDestinationURL());
            if (existingUrl != null) {
                return responseUtility.createResponse(ServiceConstants.HTTP_STATUS_UNPROCESSABLE_ENTITY,
                        ServiceConstants.RESPONSE_UNPROCESSABLE_ENTITY, ServiceConstants.ERROR_SHORT_URL_EXISTS, null);
            }

            // Generate short URL
            Urls url = new Urls();
            url.setDestinationUrl(shortUrlRequest.getDestinationURL());
            url.setShortUrl(generateShortUrl());
            url.setExpiresAt(Timestamp.valueOf(LocalDateTime.now().plusDays(defaultExpiryDays)));
            url.setClickCount(0);

            // Save URL
            Urls savedUrl = urlsRepository.save(url);

            if (savedUrl != null) {
                return responseUtility.createResponse(ServiceConstants.HTTP_STATUS_SUCCESS,
                        ServiceConstants.RESPONSE_SUCCESS, "Short URL generated successfully",
                        baseUrlPrefix + savedUrl.getShortUrl());
            } else {
                return responseUtility.createResponse(ServiceConstants.HTTP_STATUS_INTERNAL_SERVER_ERROR,
                        ServiceConstants.RESPONSE_INTERNAL_SERVER_ERROR,
                        ServiceConstants.ERROR_FAILED_GENERATE_SHORT_URL, null);
            }
        } catch (DataAccessException ex) {
            logger.error("Data access error while generating Short URL", ex);
            return responseUtility.createResponse(ServiceConstants.HTTP_STATUS_INTERNAL_SERVER_ERROR,
                    ServiceConstants.RESPONSE_INTERNAL_SERVER_ERROR, ServiceConstants.ERROR_DATABASE_ACCESS, null);
        } catch (Exception e) {
            logger.error("Unexpected error while generating Short URL", e);
            return responseUtility.createResponse(ServiceConstants.HTTP_STATUS_INTERNAL_SERVER_ERROR,
                    ServiceConstants.RESPONSE_INTERNAL_SERVER_ERROR, ServiceConstants.ERROR_UNEXPECTED, null);
        }
    }

    @Override
    @Cacheable(value = "urlsCache", key = "#shortUrl")
    public ResponseEntity<Object> getDestinationURLByShortURL(String shortUrl) {
        try {
            logger.info("getDestinationURLByShortURL service method started");

            // Validate request
            if (shortUrl == null || shortUrl.isEmpty()) {
                return responseUtility.createResponse(ServiceConstants.HTTP_STATUS_BAD_REQUEST,
                        ServiceConstants.RESPONSE_BAD_REQUEST, "Short URL cannot be empty", null);
            }

            // Fetch from repository
            Urls shortUrlExist = urlsRepository.findByShortUrl(shortUrl);

            if (shortUrlExist != null) {
                // Increment click count
                int count = shortUrlExist.getClickCount() + 1;
                shortUrlExist.setClickCount(count);
                urlsRepository.save(shortUrlExist); // Save to update click count

                return responseUtility.createResponse(ServiceConstants.HTTP_STATUS_SUCCESS,
                        ServiceConstants.RESPONSE_SUCCESS, "Destination URL fetched successfully",
                        shortUrlExist.getDestinationUrl());
            } else {
                return responseUtility.createResponse(ServiceConstants.HTTP_STATUS_NOT_FOUND,
                        ServiceConstants.RESPONSE_NOT_FOUND,
                        "Destination URL does not exist for Short URL: " + shortUrl, null);
            }
        } catch (DataAccessException ex) {
            logger.error("Database access error while fetching Destination URL", ex);
            return responseUtility.createResponse(ServiceConstants.HTTP_STATUS_INTERNAL_SERVER_ERROR,
                    ServiceConstants.RESPONSE_INTERNAL_SERVER_ERROR, ServiceConstants.ERROR_DATABASE_ACCESS, null);
        } catch (Exception e) {
            logger.error("Unexpected error while fetching Destination URL", e);
            return responseUtility.createResponse(ServiceConstants.HTTP_STATUS_INTERNAL_SERVER_ERROR,
                    ServiceConstants.RESPONSE_INTERNAL_SERVER_ERROR, ServiceConstants.ERROR_UNEXPECTED, null);
        }
    }

    @Override
    @Transactional
    @CacheEvict(value = "urlsCache", key = "#updateDestinationUrlRequest.shortenURL")
    public ResponseEntity<Object> updateDestinationUrlByShortUrl(
            UpdateDestinationUrlRequest updateDestinationUrlRequest) throws Exception {
        try {
            logger.info("updateDestinationUrlByShortUrl service method started");

            // Validate request
            if (updateDestinationUrlRequest == null || updateDestinationUrlRequest.getDestinationURL().isEmpty()
                    || updateDestinationUrlRequest.getShortenURL().isEmpty()) {
                return responseUtility.createResponse(ServiceConstants.HTTP_STATUS_BAD_REQUEST,
                        ServiceConstants.RESPONSE_BAD_REQUEST, ServiceConstants.ERROR_EMPTY_FIELDS, null);
            }

            // Fetch existing URL by short URL
            Urls shortUrlRow = urlsRepository.findByShortUrl(updateDestinationUrlRequest.getShortenURL());
            if (shortUrlRow != null) {

                // Check if new destination URL is different from existing
                logger.info("boolean: " + shortUrlRow.getDestinationUrl() + " "
                        + updateDestinationUrlRequest.getDestinationURL());

                logger.info("boolean: "
                        + shortUrlRow.getDestinationUrl().equals(updateDestinationUrlRequest.getDestinationURL()));

                if (!shortUrlRow.getDestinationUrl().equals(updateDestinationUrlRequest.getDestinationURL())) {

                    // Update destination URL in the database
                    int isUpdated = urlsRepository.updateDestinationUrlByShortUrl(
                            updateDestinationUrlRequest.getDestinationURL(),
                            updateDestinationUrlRequest.getShortenURL());

                    if (isUpdated > 0) {
                        return responseUtility.createResponse(ServiceConstants.HTTP_STATUS_SUCCESS,
                                ServiceConstants.RESPONSE_SUCCESS, "Destination URL updated successfully", true);
                    } else {
                        return responseUtility.createResponse(ServiceConstants.HTTP_STATUS_NOT_FOUND,
                                ServiceConstants.RESPONSE_NOT_FOUND, "Destination URL failed to update for Short URL: "
                                        + updateDestinationUrlRequest.getShortenURL(),
                                null);
                    }
                } else {
                    return responseUtility.createResponse(ServiceConstants.HTTP_STATUS_SUCCESS,
                            ServiceConstants.RESPONSE_SUCCESS, "Destination URL already exists", false);
                }
            } else {
                return responseUtility.createResponse(ServiceConstants.HTTP_STATUS_NOT_FOUND,
                        ServiceConstants.RESPONSE_NOT_FOUND,
                        "Short URL does not exist: " + updateDestinationUrlRequest.getShortenURL(), null);
            }
        } catch (DataAccessException ex) {
            logger.error("Database access error while updating Destination URL", ex);
            return responseUtility.createResponse(ServiceConstants.HTTP_STATUS_INTERNAL_SERVER_ERROR,
                    ServiceConstants.RESPONSE_INTERNAL_SERVER_ERROR, ServiceConstants.ERROR_DATABASE_ACCESS, null);
        } catch (Exception e) {
            logger.error("Unexpected error while updating Destination URL", e);
            return responseUtility.createResponse(ServiceConstants.HTTP_STATUS_INTERNAL_SERVER_ERROR,
                    ServiceConstants.RESPONSE_INTERNAL_SERVER_ERROR, ServiceConstants.ERROR_UNEXPECTED, null);
        }
    }

    @Override
    @CacheEvict(value = "urlsCache", key = "#updateExpireByShortUrl.shortenURL")
    public ResponseEntity<Object> updateExpireDaysByShortUrl(UpdateExpireByShortUrl updateExpireByShortUrl)
            throws Exception {
        try {
            logger.info("updateExpireDaysByShortUrl service method started");

            // Validate request
            if (updateExpireByShortUrl == null || updateExpireByShortUrl.getShortenURL() == null
                    || updateExpireByShortUrl.getAddOnExpireDays() == null) {
                return responseUtility.createResponse(ServiceConstants.HTTP_STATUS_BAD_REQUEST,
                        ServiceConstants.RESPONSE_BAD_REQUEST, ServiceConstants.ERROR_DESTINATION_URL_EMPTY, null);
            }

            // Check if short URL exists
            Urls shortUrlRow = urlsRepository.findByShortUrl(updateExpireByShortUrl.getShortenURL());
            if (shortUrlRow == null) {
                return responseUtility.createResponse(ServiceConstants.HTTP_STATUS_UNPROCESSABLE_ENTITY,
                        ServiceConstants.RESPONSE_UNPROCESSABLE_ENTITY, ServiceConstants.ERROR_SHORT_URL_NOT_FOUND,
                        null);
            }

            // Update expire date
            LocalDateTime newExpiresAt = shortUrlRow.getExpiresAt().toLocalDateTime()
                    .plusDays(updateExpireByShortUrl.getAddOnExpireDays());
            shortUrlRow.setExpiresAt(Timestamp.valueOf(newExpiresAt));

            // Save URL
            Urls savedUrl = urlsRepository.save(shortUrlRow);

            if (savedUrl != null) {
                return responseUtility.createResponse(ServiceConstants.HTTP_STATUS_SUCCESS,
                        ServiceConstants.RESPONSE_SUCCESS, "Expire days updated successfully", true);
            } else {
                return responseUtility.createResponse(ServiceConstants.HTTP_STATUS_INTERNAL_SERVER_ERROR,
                        ServiceConstants.RESPONSE_INTERNAL_SERVER_ERROR,
                        ServiceConstants.ERROR_FAILED_UPDATE_EXPIRE_DAYS, false);
            }
        } catch (DataAccessException ex) {
            logger.error("Data access error while updating Expire Days", ex);
            return responseUtility.createResponse(ServiceConstants.HTTP_STATUS_INTERNAL_SERVER_ERROR,
                    ServiceConstants.RESPONSE_INTERNAL_SERVER_ERROR, ServiceConstants.ERROR_DATABASE_ACCESS, null);
        } catch (Exception e) {
            logger.error("Unexpected error while updating Expire Days", e);
            return responseUtility.createResponse(ServiceConstants.HTTP_STATUS_INTERNAL_SERVER_ERROR,
                    ServiceConstants.RESPONSE_INTERNAL_SERVER_ERROR, ServiceConstants.ERROR_UNEXPECTED, null);
        }
    }

    // Method to generate random short URL
    private String generateShortUrl() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        return RandomStringUtils.random(shortUrlLength, characters);
    }
}
