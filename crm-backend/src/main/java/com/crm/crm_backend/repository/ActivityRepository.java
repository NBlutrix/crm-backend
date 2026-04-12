package com.crm.crm_backend.repository;

import com.crm.crm_backend.model.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
    List<Activity> findByContactIdOrderByCreatedAtDesc(Long contactId);
}
