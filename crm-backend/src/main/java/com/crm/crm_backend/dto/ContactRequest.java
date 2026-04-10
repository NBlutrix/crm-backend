package com.crm.crm_backend.dto;

import lombok.Data;

@Data
public class ContactRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String position;
    private Long companyId;
}