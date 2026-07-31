package org.kafka.producer.service;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaService {
	
	@Autowired
	KafkaTemplate<String, String> kafkaTemplate;
	
	public void publishMessage(String _topic, String _message) {
		System.out.println("publishMessage() from service :::::::::::::::START::::::::::::::::");
		ProducerRecord<String, String> producer=new ProducerRecord<String, String>(_topic,null, _message);
		kafkaTemplate.send(producer);
		System.out.println();
		System.out.println("publishMessage() from service :::::::::::::::END::::::::::::::::");

	}

}
