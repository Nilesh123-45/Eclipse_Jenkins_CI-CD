package com.kodewala.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.kodewala.dto.response.ErrorResponse;

@RestControllerAdvice
public class GlobalException {
	
	@ExceptionHandler(InValidProductCatalog.class)
	public ResponseEntity<ErrorResponse> invalidDetails(InValidProductCatalog invalid){
		
		ErrorResponse error=new ErrorResponse("EBAY-MSG-TXN1276",invalid.getMessage());
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}
	
	@ExceptionHandler(Exception.class)
	public void invalidDetails(	Exception invalid){
		
	
	}

}
