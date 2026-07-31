package org.kafka.producer.service;

import java.util.Random;

import org.kafka.producer.dto.request.BookingRequest;
import org.kafka.producer.dto.response.BookingResponse;
import org.kafka.producer.entity.BookingEntity;
import org.kafka.producer.repos.BookingRepos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookingService {
	
	private final BookingRepos bookingRepos;
	private final KafkaService kafkaService;
	
	public BookingService(BookingRepos bookingRepos, KafkaService kafkaService) {
		this.bookingRepos=bookingRepos;
		this.kafkaService=kafkaService;
	}
	
	public BookingResponse doBooking(BookingRequest bookingRequest) {
		
		BookingEntity bookingEntity=new BookingEntity();
		BookingResponse bookingResponse=new BookingResponse();
		
		
		bookingEntity.setPassengerName(bookingRequest.getPassengerName());
		bookingEntity.setPhoneNum(bookingRequest.getPhoneNum());
		bookingEntity.setEmail(bookingRequest.getEmail());
		bookingEntity.setAge(bookingRequest.getAge());
		bookingEntity.setGender(bookingRequest.getGender());
		
		bookingEntity.setBoardingpoint(bookingRequest.getBoardingPoint());
		bookingEntity.setDestination(bookingRequest.getDestination());
		bookingEntity.setDate(bookingRequest.getDate());
		bookingEntity.setTravelClass(bookingRequest.getTravelClass());
		bookingEntity.setAmount(calculateAmount(bookingRequest.getTravelClass()));
		
		bookingEntity.setPnr(generatePNR());
		bookingEntity.setBookingStatus(generateBookingStatus());
		
		BookingEntity savedBooking=bookingRepos.save(bookingEntity);
		
		bookingResponse.setPassengerName(savedBooking.getPassengerName());
		bookingResponse.setEmail(savedBooking.getEmail());
		bookingResponse.setPhoneNum(savedBooking.getPhoneNum());
		bookingResponse.setAge(savedBooking.getAge());
		
		bookingResponse.setBoarding(savedBooking.getBoardingpoint());
		bookingResponse.setDestination(savedBooking.getDestination());
		
		bookingResponse.setBookingStatus(savedBooking.getBookingStatus());
		bookingResponse.setPnr(savedBooking.getPnr());
		
		bookingResponse.setBookingClass(savedBooking.getTravelClass());
		bookingResponse.setBookingId(savedBooking.getBookingId());
		
		String topic="booking_details";
		String message=null;
		
		for(int i=0;i<500;i++) {
			message="sending booking response to the passenger having message number: "+i;
			kafkaService.publishMessage(topic, message);
		}
		
		return bookingResponse;
	}
	
	 private static  String generatePNR() {
	        return String.format("%010d", Math.abs(new Random().nextLong() % 10000000000L));
	    }

	    // === 2. Auto-calculate Amount (mock IRCTC style) ===
	    private static  double calculateAmount(String travelClass) {
	        double baseFare = 500.0; // base fare
	        double multiplier = switch (travelClass.toUpperCase()) {
	            case "SL" -> 1.0;   // Sleeper
	            case "3A" -> 2.0;   // AC 3 Tier
	            case "2A" -> 2.5;   // AC 2 Tier
	            case "1A" -> 3.5;   // AC First Class
	            default -> 1.0;
	        };
	        return baseFare * multiplier;
	    }
	
	private static String generateBookingStatus() {
	    Random random = new Random();
	    int value = random.nextInt(100); // 0–99

	    if (value < 70) {
	        return "CONFIRMED";   // 70% chance
	    } else if (value < 90) {
	        return "WAITLIST";    // 20% chance
	    } else {
	        return "CANCELLED";   // 10% chance
	    }
	}

}
