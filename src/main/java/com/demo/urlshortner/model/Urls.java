package com.demo.urlshortner.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "urls")
@Getter
@Setter
public class Urls {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "long_url", nullable = false)
	private String destinationUrl;

	@Column(name = "short_url", nullable = false, unique = true, length = 30)
	private String shortUrl;

	@Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private Timestamp createdAt;

	@Column(name = "expires_at")
	private Timestamp expiresAt;

	@Column(name = "click_count", nullable = false)
	private int clickCount;

	@PrePersist
	protected void onCreate() {
		createdAt = Timestamp.valueOf(LocalDateTime.now());
	}
}
