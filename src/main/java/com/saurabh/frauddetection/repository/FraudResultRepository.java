package com.saurabh.frauddetection.repository;

import com.saurabh.frauddetection.entity.FraudResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FraudResultRepository extends JpaRepository<FraudResult, String> {
}
