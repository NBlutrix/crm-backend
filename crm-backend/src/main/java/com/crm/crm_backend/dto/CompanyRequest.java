package com.crm.crm_backend.dto;

import lombok.Data;

@Data
public class CompanyRequest {
    private String name;
    private String industry;
    private String website;
    private String phone;
}