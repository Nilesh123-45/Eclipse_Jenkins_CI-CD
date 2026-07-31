package org.kafka.producer.controller;

import org.kafka.producer.dto.request.BookingRequest;
import org.kafka.producer.dto.response.BookingResponse;
import org.kafka.producer.service.BookingService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/irctc/api/v3.0/")
public class BookingController {
	
	private final BookingService bookingService;
	
	public BookingController(BookingService bookingService){
		this.bookingService=bookingService;
	}
	
	@PostMapping("doBook")
	public BookingResponse makeBooking(@RequestBody BookingRequest bookingRequest) {
		return bookingService.doBooking(bookingRequest);
	}

}
