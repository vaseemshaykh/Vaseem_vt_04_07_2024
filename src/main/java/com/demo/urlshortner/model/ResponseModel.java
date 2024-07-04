package com.demo.urlshortner.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ResponseModel {

	private Integer statuscode;

	private String status;

	private String message;

	private Object data;

}