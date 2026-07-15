package com.eclipse.irctc.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.eclipse.irctc.response.ErrorResponse;
@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(InValidAmount.class)
	public ResponseEntity<ErrorResponse> handleInValidAmount(InValidAmount invalidAmount){
		ErrorResponse error=new ErrorResponse("INVALID-AMOUNT(MONEY7895)",invalidAmount.getMessage());
		
		//so the response entity contains the response(body) as well as the http status code
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
		
	}
	
	@ExceptionHandler(InValidDetailsException.class)
	public ResponseEntity<ErrorResponse> handleInValidDetails(InValidDetailsException inValidDetailsException)
	{
	
		ErrorResponse error=new ErrorResponse("INVALID-DETAILS (DETAILS_REGIST159)",inValidDetailsException.getMessage());
		
		//so the response entity contains the response(body) as well as the http status code
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleInValidAmount(Exception exception){
		ErrorResponse error=new ErrorResponse("INVALID",exception.getMessage());
		
		//so the response entity contains the response as well as the http status code
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
		
	}

}
