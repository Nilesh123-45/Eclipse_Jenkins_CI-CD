package com.eclipse.irctc.service;

import java.util.concurrent.ThreadLocalRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eclipse.irctc.entity.BookingEntity;
import com.eclipse.irctc.exception.InValidAmount;
import com.eclipse.irctc.exception.InValidDetailsException;
import com.eclipse.irctc.repository.BookingRepos;
import com.eclipse.irctc.request.BookingRequest;
import com.eclipse.irctc.response.BookingResponse;

import jakarta.transaction.Transactional;

@Service
public class BookingService {
	
	@Autowired
	BookingRepos bookingRepos;
	
	@Transactional
	public BookingResponse doBooking(BookingRequest request) {
		

		    
		        System.out.println("doBooking() from service started------------");

		        BookingEntity bookingEntity = new BookingEntity();
		        BookingResponse bookingResponse = new BookingResponse();
		        
		        if(request.getPassengerName() == null || request.getPassengerName().isBlank()){
		            throw new InValidDetailsException("Passenger name is mandatory.");
		        }
		        
		        if(request.getBoardingStation()==null || request.getBoardingStation().isBlank() || request.getDestination()==null || request.getBoardingStation().isBlank()) {
		        	throw new InValidDetailsException("Boarding or Destination can be emptied....");
		        }

		        // Business Validation
		        if (request.getAmount() <= 0) {
		            throw new InValidAmount("Amount " + request.getAmount() + " is invalid.");
		        }

		        bookingEntity.setPassengerName(request.getPassengerName());
		        bookingEntity.setGender(request.getGender());
		        bookingEntity.setBoardingStation(request.getBoardingStation());
		        bookingEntity.setDestination(request.getDestination());
		        bookingEntity.setAmount(request.getAmount());
		        bookingEntity.setPnrNumber(generatePnr());
		        bookingEntity.setPnrStatus(generatePnrStatus());
		        

		        BookingEntity savedBooking = bookingRepos.save(bookingEntity);

		        bookingResponse.setBookingId(savedBooking.getBookingId());
		        bookingResponse.setPassengerName(savedBooking.getPassengerName());
		        bookingResponse.setGender(savedBooking.getGender());
		        bookingResponse.setAmount(savedBooking.getAmount());
		        bookingResponse.setBoardingStation(savedBooking.getBoardingStation());
		        bookingResponse.setDestination(savedBooking.getDestination());
		        bookingResponse.setPnrNumber(savedBooking.getPnrNumber());
		        bookingResponse.setBookingStatus(savedBooking.getPnrStatus());

		        return bookingResponse;
		    }
	
			private static String generatePnr() {
		
			    long pnr = ThreadLocalRandom.current()
			            .nextLong(1000000000L, 10000000000L);
		
			    return String.valueOf(pnr);
			}
			private static String generatePnrStatus() {

			    double probability = Math.random();

			    if (probability < 0.70) {
			        return "CNF";
			    } else if (probability < 0.90) {
			        return "RAC";
			    } else {
			        return "WL";
			    }
			}
	

}
