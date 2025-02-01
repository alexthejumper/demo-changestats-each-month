package com.example.demo5;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "company")
@Getter
@Setter
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID companyId;

    @Column
    private String companyName;

    @Column
    private LocalDateTime createdDt;

    @Column
    private String createdBy;

    @Column
    private LocalDateTime updatedDt;

    @Column
    private String updatedBy;
}
