package com.crm.crm_backend.controller;

import com.crm.crm_backend.dto.ActivityRequest;
import com.crm.crm_backend.dto.DashboardStats;
import com.crm.crm_backend.dto.DealRequest;
import com.crm.crm_backend.dto.DealResponse;
import com.crm.crm_backend.model.Activity;
import com.crm.crm_backend.model.DealStage;
import com.crm.crm_backend.service.DealService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class DealController {

    private final DealService dealService;

    @GetMapping("/api/deals")
    public ResponseEntity<List<DealResponse>> getAllDeals(
            @RequestParam(required = false) DealStage stage) {
        return ResponseEntity.ok(dealService.getAllDeals(stage));
    }

    @PostMapping("/api/deals")
    public ResponseEntity<DealResponse> createDeal(@RequestBody DealRequest request) {
        return ResponseEntity.ok(dealService.createDeal(request));
    }

    @PutMapping("/api/deals/{id}")
    public ResponseEntity<DealResponse> updateDeal(
            @PathVariable Long id,
            @RequestBody DealRequest request) {
        return ResponseEntity.ok(dealService.updateDeal(id, request));
    }

    @PutMapping("/api/deals/{id}/stage")
    public ResponseEntity<DealResponse> updateStage(
            @PathVariable Long id,
            @RequestParam DealStage stage) {
        return ResponseEntity.ok(dealService.updateStage(id, stage));
    }

    @PostMapping("/api/activities")
    public ResponseEntity<Activity> createActivity(@RequestBody ActivityRequest request) {
        return ResponseEntity.ok(dealService.createActivity(request));
    }

    @GetMapping("/api/activities")
    public ResponseEntity<List<Activity>> getActivities(
            @RequestParam Long contactId) {
        return ResponseEntity.ok(dealService.getActivitiesForContact(contactId));
    }

    @GetMapping("/api/dashboard/stats")
    public ResponseEntity<DashboardStats> getDashboardStats() {
        return ResponseEntity.ok(dealService.getDashboardStats());
    }
}