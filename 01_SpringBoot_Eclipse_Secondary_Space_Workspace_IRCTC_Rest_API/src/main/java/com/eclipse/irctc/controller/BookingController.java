package com.eclipse.irctc.controller;

import com.eclipse.irctc.repository.BookingRepos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eclipse.irctc.request.BookingRequest;
import com.eclipse.irctc.response.BookingResponse;
import com.eclipse.irctc.service.BookingService;

@RestController
@RequestMapping("/api/v1.0/test/")
public class BookingController {
	
	private final BookingRepos bookingRepos;
	@Autowired
	BookingService bookingService;

	BookingController(BookingRepos bookingRepos) {
		this.bookingRepos = bookingRepos;
	}
	
	@PostMapping("uploadPassenger")
	public BookingResponse doUpload(@RequestBody BookingRequest request) {
		System.out.println("From controller doUpload() started........");
		System.out.println("-------------------------------");
		BookingResponse booked=bookingService.doBooking(request);
		System.out.println("-------------------------------");
		System.out.println("From controller doUpload() END.......");
		return booked;
		
	}

}
