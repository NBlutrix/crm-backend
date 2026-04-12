package com.crm.crm_backend.dto;

import com.crm.crm_backend.model.DealStage;
import lombok.Data;

@Data
public class DealRequest {
    private String title;
    private Double value;
    private DealStage stage;
    private Long contactId;
}
