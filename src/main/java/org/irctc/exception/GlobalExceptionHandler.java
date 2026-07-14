package org.irctc.exception;

import org.irctc.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(InvalidException.class)
	public ResponseEntity<ErrorResponse> handleInvalidPayException(InvalidException invalid){
		
		ErrorResponse errorResponse=new ErrorResponse("ERROR_1234",invalid.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
	}
	
	@ExceptionHandler(Exception.class)
	public void handleInException(Exception exception)
	{
		// this block will get executed if Exception
	}
	

}
