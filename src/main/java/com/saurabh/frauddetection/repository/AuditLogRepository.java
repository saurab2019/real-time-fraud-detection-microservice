package com.saurabh.frauddetection.repository;

import com.saurabh.frauddetection.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long>
{
}
