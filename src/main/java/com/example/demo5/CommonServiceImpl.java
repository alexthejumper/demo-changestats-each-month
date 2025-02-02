package com.example.demo5;

import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
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

    @Override
    public void updateVisitorTest(Visitor visitor, VisitorTestDto visitorTestDto) {
        visitor.setFirstName(visitorTestDto.firstName());
        visitor.setLastName(visitorTestDto.lastName());
        String encryptedContact = encryptionUtil.encrypt(visitorTestDto.contactNumber());
        visitor.setContactNumber(encryptedContact);
    }

    @Override
    public void updateCompanyTest(Visitor visitor, VisitorTestDto visitorTestDto) {
        Optional.ofNullable(visitor.getCompany()).ifPresentOrElse(companyDto -> {
            if (companyDto.getCompanyId() != null) {
                // Fetch existing company or throw an exception if not found
                Company existingCompany = companyRepository.findById(companyDto.getCompanyId())
                        .orElseThrow(() -> new ResourceNotFoundException("Company with ID " + companyDto.getCompanyId() + " not found"));

                // Update company name if it has changed
                if (companyDto.getCompanyName() != null && !companyDto.getCompanyName().isBlank()
                        && !existingCompany.getCompanyName().equalsIgnoreCase(companyDto.getCompanyName())) {
                    existingCompany.setCompanyName(companyDto.getCompanyName());
                    companyRepository.save(existingCompany);
                }
                visitor.setCompany(existingCompany);
            } else if (companyDto.getCompanyName() != null && !companyDto.getCompanyName().isBlank()) {
                // Check if company exists by name, otherwise create a new one
                Company companyByName = companyRepository.findByCompanyNameIgnoreCase(companyDto.getCompanyName())
                        .orElseGet(() -> {
                            Company newCompany = new Company();
                            newCompany.setCompanyName(companyDto.getCompanyName());
                            return companyRepository.save(newCompany);
                        });

                // Assign the newly created/found company to the visitor
                visitor.setCompany(companyByName);
            } else {
                visitor.setCompany(null);
            }
        }, () -> visitor.setCompany(null)); // If visitor.getCompany() is null, set visitor's company to null
    }
}
