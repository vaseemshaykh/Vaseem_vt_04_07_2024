package com.demo.urlshortner.response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.demo.urlshortner.model.ResponseModel;

public class ResponseUtility {

	@Autowired
	ResponseModel responseModel;

	public ResponseEntity<Object> createResponse(Integer statusCode, String success, String message, Object data) {
		responseModel.setStatuscode(statusCode);
		responseModel.setStatus(success);
		responseModel.setMessage(message);
		responseModel.setData(data);

		if (success.equalsIgnoreCase("SUCCESS")) {
			return ResponseEntity.status(HttpStatus.OK).body(responseModel);
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseModel);
		}

	}

}