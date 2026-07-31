package org.kafka.consumer.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {
	
	@KafkaListener(topics = "booking_details", groupId = "CGB")
	public void consumeEvent(ConsumerRecord<String, String> consumerRecord) {
		System.out.println("KafkaConsumerService.consume()......");
		String message = consumerRecord.value();
		System.out.println(" Message received :::: " + message);
	}

}
