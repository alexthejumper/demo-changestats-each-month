package com.example.demo5;

import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommonServiceImpl implements CommonService {

    private final EncryptionUtil encryptionUtil;
    private final CompanyRepository companyRepository;

    @Override
    public void updateVisitor(Visitor visitor, VisitorDto visitorDto) {
        visitor.setFirstName(visitorDto.firstName());
        visitor.setLastName(visitorDto.lastName());
        String encryptedContact = encryptionUtil.encrypt(visitorDto.contactNumber());
        visitor.setContactNumber(encryptedContact);
    }

    @Override
    public void updateCompany(Visitor visitor, VisitorDto visitorDto) {
        Optional.ofNullable(visitor.getCompany())
                .ifPresent(companyDto -> {
                    if (companyDto.getCompanyId() != null) {
                        Company existingCompany = companyRepository.findById(companyDto.getCompanyId()).orElseThrow(() -> new ResourceNotFoundException("Company with ID " + companyDto.getCompanyId() + " not found"));

                        if (companyDto.getCompanyName() != null && !companyDto.getCompanyName().isBlank() && !existingCompany.getCompanyName().equalsIgnoreCase(companyDto.getCompanyName())) {
                            existingCompany.setCompanyName(companyDto.getCompanyName());
                            companyRepository.save(existingCompany);
                        }
                        visitor.setCompany(existingCompany);
                    }
                    else if (companyDto.getCompanyName() != null && !companyDto.getCompanyName().isBlank()) {
                        Company companyByName = companyRepository.findByCompanyNameIgnoreCase(companyDto.getCompanyName()).orElseGet(() -> {
                            Company newCompany = new Company();
                            newCompany.setCompanyName(companyDto.getCompanyName());
                            return companyRepository.save(newCompany);
                        });
                    }
                    else {
                        visitor.setCompany(null);
                    }
                });
    }
}
