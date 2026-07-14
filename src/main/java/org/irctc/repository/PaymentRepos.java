package org.irctc.repository;

import org.irctc.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepos extends JpaRepository<PaymentEntity, Long>{

}
