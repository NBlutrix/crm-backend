package com.crm.crm_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.Map;

@Data
@AllArgsConstructor
public class DashboardStats {
    private Long totalContacts;
    private Long totalDeals;
    private Double totalWonValue;
    private Long wonDeals;
    private Long lostDeals;
    private Double winRate;
    private Map<String, Long> dealsByStage;
}