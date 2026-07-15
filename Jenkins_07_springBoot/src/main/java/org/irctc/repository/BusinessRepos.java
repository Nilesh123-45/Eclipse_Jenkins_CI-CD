package org.irctc.repository;

import org.irctc.entity.BusinessEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BusinessRepos extends JpaRepository<BusinessEntity, Long>{

}
