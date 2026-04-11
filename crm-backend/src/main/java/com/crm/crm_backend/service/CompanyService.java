package com.crm.crm_backend.service;

import com.crm.crm_backend.dto.CompanyRequest;
import com.crm.crm_backend.model.Company;
import com.crm.crm_backend.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;

    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    public Company getCompanyById(Long id) {
        return companyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Kompanija nije pronadjena"));
    }

    public Company createCompany(CompanyRequest request){

        Company company = new Company();
        company.setName(request.getName());
        company.setIndustry(request.getIndustry());
        company.setWebsite(request.getWebsite());
        company.setPhone(request.getPhone());
        return companyRepository.save(company);
    }

    public Company updateCompany(Long id, CompanyRequest request){

        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Kompanija nije pronadjena"));
        company.setName(request.getName());
        company.setIndustry(request.getIndustry());
        company.setWebsite(request.getWebsite());
        company.setPhone(request.getPhone());
        return companyRepository.save(company);
    }

    public void deleteCompany(Long id) {
        companyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Kompanija nije pronađena"));
        companyRepository.deleteById(id);
    }
}
