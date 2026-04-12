package com.crm.crm_backend.dto;

import com.crm.crm_backend.model.DealStage;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class DealResponse {
    private Long id;
    private String title;
    private Double value;
    private DealStage stage;
    private String contactName;
    private String assignedTo;
    private LocalDateTime createdAt;
}