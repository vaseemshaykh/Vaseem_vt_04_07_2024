package com.demo.urlshortner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.demo.urlshortner.model.Urls;

@Repository
public interface UrlsRepository extends JpaRepository<Urls, Long> {

    // Find Urls by destination URL
    Urls findByDestinationUrl(String destinationUrl);

    // Find Urls by short URL
    Urls findByShortUrl(String shortUrl);

    // Update destination URL by short URL
    @Modifying
    @Query("UPDATE Urls u SET u.destinationUrl = :destinationUrl WHERE u.shortUrl = :shortUrl")
    int updateDestinationUrlByShortUrl(@Param("destinationUrl") String destinationUrl, @Param("shortUrl") String shortUrl);

}
