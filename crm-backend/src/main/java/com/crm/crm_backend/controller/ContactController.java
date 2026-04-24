package com.crm.crm_backend.controller;

import com.crm.crm_backend.dto.ContactRequest;
import com.crm.crm_backend.dto.ContactResponse;
import com.crm.crm_backend.service.ContactService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contacts")
@RequiredArgsConstructor
@Tag(name = "Kontakti", description = "Upravljanje kontaktima")
public class ContactController {

    private final ContactService contactService;

    @GetMapping
    @Operation(summary = "Lista svih kontakata sa paginacijom i pretragom")
    public ResponseEntity<Page<ContactResponse>> getAllContacts(
            @RequestParam(required = false) String search,
            Pageable pageable) {
        return ResponseEntity.ok(contactService.getAllContacts(search, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContactResponse> getContact(@PathVariable Long id) {
        return ResponseEntity.ok(contactService.getContactById(id));
    }

    @PostMapping
    @Operation(summary = "Kreiranje novog kontakta")
    public ResponseEntity<ContactResponse> createContact(
            @RequestBody ContactRequest request) {
        return ResponseEntity.ok(contactService.createContact(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Izmena kontakta")
    public ResponseEntity<ContactResponse> updateContact(
            @PathVariable Long id,
            @RequestBody ContactRequest request) {
        return ResponseEntity.ok(contactService.updateContact(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Brisanje kontakta — samo ADMIN")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteContact(@PathVariable Long id) {
        contactService.deleteContact(id);
        return ResponseEntity.noContent().build();
    }
}