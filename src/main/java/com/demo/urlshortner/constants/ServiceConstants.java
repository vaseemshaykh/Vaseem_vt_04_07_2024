package com.demo.urlshortner.constants;

public class ServiceConstants {

	// HTTP Status Codes
	public static final int HTTP_STATUS_SUCCESS = 200;
	public static final int HTTP_STATUS_BAD_REQUEST = 400;
	public static final int HTTP_STATUS_UNPROCESSABLE_ENTITY = 422;
	public static final int HTTP_STATUS_INTERNAL_SERVER_ERROR = 500;
	public static final int HTTP_STATUS_NOT_FOUND = 404;

	// Response Messages
	public static final String RESPONSE_SUCCESS = "Success";
	public static final String RESPONSE_BAD_REQUEST = "Bad Request";
	public static final String RESPONSE_UNPROCESSABLE_ENTITY = "Unprocessable Entity";
	public static final String RESPONSE_INTERNAL_SERVER_ERROR = "Internal Server Error";
	public static final String RESPONSE_NOT_FOUND = "Not Found";

	// Error Messages
	public static final String ERROR_DESTINATION_URL_EMPTY = "Destination URL cannot be empty";
	public static final String ERROR_SHORT_URL_EXISTS = "Short URL already exists";
	public static final String ERROR_FAILED_GENERATE_SHORT_URL = "Failed to generate short URL";
	public static final String ERROR_DATABASE_ACCESS = "Database access error";
	public static final String ERROR_UNEXPECTED = "Unexpected error";
	public static final String ERROR_EMPTY_FIELDS = "Empty fields error";
	public static final String ERROR_SHORT_URL_NOT_FOUND = "Short URL does not exist";
	public static final String ERROR_FAILED_UPDATE_EXPIRE_DAYS = "Failed to update expiration days for Short URL";

}
