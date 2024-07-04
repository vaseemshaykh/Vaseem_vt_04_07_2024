package com.demo.urlshortner.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateExpireByShortUrl {

	String shortenURL;

	Integer addOnExpireDays;

}
