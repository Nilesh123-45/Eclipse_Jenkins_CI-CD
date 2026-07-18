package org.redis.exception;

import org.redis.dto.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalException {
	
	@ExceptionHandler(InValidDetailsEcception.class)
	public ResponseEntity<ErrorResponse> invalidDetails(InValidDetailsEcception invalidDetails){
		ErrorResponse err=new ErrorResponse("ERROR_7847",invalidDetails.getMessage());
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
	}
	
	@ExceptionHandler(Exception.class)
	public void handleInException(Exception exception)
	{
		// this block will get executed if Exception
	}
}
