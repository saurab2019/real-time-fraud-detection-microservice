package com.saurabh.frauddetection.repository;

import com.saurabh.frauddetection.entity.FraudResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IFraudResultRepository extends JpaRepository<FraudResult, String> {
}
