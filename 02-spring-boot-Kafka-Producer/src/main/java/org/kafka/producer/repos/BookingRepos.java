package org.kafka.producer.repos;

import org.kafka.producer.entity.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepos extends JpaRepository<BookingEntity, Integer>{

}
