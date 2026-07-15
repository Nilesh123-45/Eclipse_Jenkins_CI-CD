package com.eclipse.irctc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eclipse.irctc.entity.BookingEntity;

@Repository
public interface BookingRepos extends JpaRepository<BookingEntity, Integer>{

}
