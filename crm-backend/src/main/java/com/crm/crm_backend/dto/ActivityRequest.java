package com.crm.crm_backend.dto;

import lombok.Data;

@Data
public class ActivityRequest {
    private String type;
    private String description;
    private Long contactId;
}