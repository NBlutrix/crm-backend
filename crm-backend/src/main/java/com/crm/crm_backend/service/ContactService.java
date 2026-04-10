package com.crm.crm_backend.service;

import com.crm.crm_backend.dto.ContactRequest;
import com.crm.crm_backend.dto.ContactResponse;
import com.crm.crm_backend.model.Company;
import com.crm.crm_backend.model.Contact;
import com.crm.crm_backend.model.User;
import com.crm.crm_backend.repository.CompanyRepository;
import com.crm.crm_backend.repository.ContactRepository;
import com.crm.crm_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContactService {

    private final ContactRepository contactRepository;
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;

    public Page<ContactResponse> getAllContacts(String search, Pageable pageable) {
        Page<Contact> contacts;
        if (search != null && !search.isEmpty()) {
            contacts = contactRepository
                    .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
                            search, search, pageable);
        } else {
            contacts = contactRepository.findAll(pageable);
        }
        return contacts.map(this::toResponse);
    }

    public ContactResponse getContactById(Long id) {
        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Kontakt nije pronađen"));
        return toResponse(contact);
    }

    public ContactResponse createContact(ContactRequest request) {
        Contact contact = new Contact();
        contact.setFirstName(request.getFirstName());
        contact.setLastName(request.getLastName());
        contact.setEmail(request.getEmail());
        contact.setPhone(request.getPhone());
        contact.setPosition(request.getPosition());

        if (request.getCompanyId() != null) {
            Company company = companyRepository.findById(request.getCompanyId())
                    .orElseThrow(() -> new RuntimeException("Kompanija nije pronađena"));
            contact.setCompany(company);
        }

        String email = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Korisnik nije pronađen"));
        contact.setAssignedTo(currentUser);

        return toResponse(contactRepository.save(contact));
    }

    public ContactResponse updateContact(Long id, ContactRequest request) {
        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Kontakt nije pronađen"));

        contact.setFirstName(request.getFirstName());
        contact.setLastName(request.getLastName());
        contact.setEmail(request.getEmail());
        contact.setPhone(request.getPhone());
        contact.setPosition(request.getPosition());

        if (request.getCompanyId() != null) {
            Company company = companyRepository.findById(request.getCompanyId())
                    .orElseThrow(() -> new RuntimeException("Kompanija nije pronađena"));
            contact.setCompany(company);
        }

        return toResponse(contactRepository.save(contact));
    }

    public void deleteContact(Long id) {
        contactRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Kontakt nije pronađen"));
        contactRepository.deleteById(id);
    }

    private ContactResponse toResponse(Contact contact) {
        ContactResponse response = new ContactResponse();
        response.setId(contact.getId());
        response.setFirstName(contact.getFirstName());
        response.setLastName(contact.getLastName());
        response.setEmail(contact.getEmail());
        response.setPhone(contact.getPhone());
        response.setPosition(contact.getPosition());
        if (contact.getCompany() != null) {
            response.setCompanyName(contact.getCompany().getName());
        }
        if (contact.getAssignedTo() != null) {
            response.setAssignedTo(contact.getAssignedTo().getEmail());
        }
        return response;
    }
}