//package com.demo.urlshortner;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//import java.sql.Timestamp;
//import java.time.LocalDateTime;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.ResponseEntity;
//
//import com.demo.urlshortner.model.Urls;
//import com.demo.urlshortner.repository.UrlsRepository;
//import com.demo.urlshortner.request.ShortUrlRequest;
//import com.demo.urlshortner.request.UpdateDestinationUrlRequest;
//import com.demo.urlshortner.request.UpdateExpireByShortUrl;
//import com.demo.urlshortner.response.ResponseUtility;
//import com.demo.urlshortner.service.impl.UrlService;
//
//@SpringBootTest
//public class UrlServiceTests {
//
//    @Mock
//    private UrlsRepository urlsRepository;
//
//    @Mock
//    private ResponseUtility responseUtility;
//
//    @InjectMocks
//    private UrlService urlService;
//
//    @Value("${url.expire.days.default-value}")
//    private int defaultExpiryDays;
//
//    @Value("${base.url.prefix}")
//    private String baseUrlPrefix;
//
//    @BeforeEach
//    public void setup() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    public void testShortenUrl_Success() {
//        // Arrange
//        ShortUrlRequest request = new ShortUrlRequest();
//        request.setDestinationURL("https://example.com");
//
//        Urls url = new Urls();
//        url.setDestinationUrl(request.getDestinationURL());
//        url.setShortUrl("abc123");
//        url.setExpiresAt(Timestamp.valueOf(LocalDateTime.now().plusDays(defaultExpiryDays)));
//        url.setClickCount(0);
//
//        when(urlsRepository.findByDestinationUrl(any())).thenReturn(null);
//        when(urlsRepository.save(any(Urls.class))).thenReturn(url);
//
//        // Act
//        ResponseEntity<Object> response = urlService.shortenUrl(request);
//
//        // Assert
//        assertNotNull(response); // Ensure response is not null
//        assertEquals(200, response.getStatusCode().value()); // Check status code
//        assertEquals("Short URL generated successfully: " + baseUrlPrefix + "abc123", response.getBody()); // Check response body
//
//        // Additional verification
//        verify(urlsRepository, times(1)).save(any(Urls.class)); // Verify save method called once
//    }
//
//    @Test
//    public void testGetDestinationURLByShortURL_Success() {
//        // Arrange
//        String shortUrl = "abc123";
//        Urls url = new Urls();
//        url.setDestinationUrl("https://example.com");
//
//        when(urlsRepository.findByShortUrl(any())).thenReturn(url);
//        when(urlsRepository.save(any(Urls.class))).thenReturn(url);
//
//        // Act
//        ResponseEntity<Object> response = urlService.getDestinationURLByShortURL(shortUrl);
//
//        // Assert
//        assertEquals(200, response.getStatusCode().value());
//        assertEquals("Destination URL fetched successfully: https://example.com", response.getBody());
//        verify(urlsRepository, times(1)).save(any(Urls.class));
//    }
//
//    @Test
//    public void testUpdateDestinationUrlByShortUrl_Success() throws Exception {
//        // Arrange
//        UpdateDestinationUrlRequest request = new UpdateDestinationUrlRequest();
//        request.setShortenURL("abc123");
//        request.setDestinationURL("https://newexample.com");
//
//        Urls url = new Urls();
//        url.setShortUrl("abc123");
//        url.setDestinationUrl("https://example.com");
//
//        when(urlsRepository.findByShortUrl(any())).thenReturn(url);
//        when(urlsRepository.updateDestinationUrlByShortUrl(anyString(), anyString())).thenReturn(1);
//
//        // Act
//        ResponseEntity<Object> response = urlService.updateDestinationUrlByShortUrl(request);
//
//        // Assert
//        assertEquals(200, response.getStatusCode().value());
//        assertEquals("Destination URL updated successfully", response.getBody());
//        verify(urlsRepository, times(1)).updateDestinationUrlByShortUrl(anyString(), anyString());
//    }
//
//    @Test
//    public void testUpdateExpireDaysByShortUrl_Success() throws Exception {
//        // Arrange
//        UpdateExpireByShortUrl request = new UpdateExpireByShortUrl();
//        request.setShortenURL("abc123");
//        request.setAddOnExpireDays(10);
//
//        Urls url = new Urls();
//        url.setShortUrl("abc123");
//        url.setExpiresAt(Timestamp.valueOf(LocalDateTime.now().plusDays(5)));
//
//        when(urlsRepository.findByShortUrl(any())).thenReturn(url);
//        when(urlsRepository.save(any(Urls.class))).thenReturn(url);
//
//        // Act
//        ResponseEntity<Object> response = urlService.updateExpireDaysByShortUrl(request);
//
//        // Assert
//        assertEquals(200, response.getStatusCode().value());
//        assertEquals("Expire Days added successfully: " + baseUrlPrefix + "abc123", response.getBody());
//        verify(urlsRepository, times(1)).save(any(Urls.class));
//    }
//}
