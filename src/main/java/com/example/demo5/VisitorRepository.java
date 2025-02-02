package com.example.demo5;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface VisitorRepository extends JpaRepository<Visitor, UUID> {
    List<Visitor> findByFirstNameAndLastNameContainsIgnoreCaseAndCompany_CompanyNameContainsIgnoreCase(String firstName, String lastName, String companyName);

    Visitor findByFirstNameAndLastName(String firstName, String lastName);
}
