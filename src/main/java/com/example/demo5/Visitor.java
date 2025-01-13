package com.example.demo5;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "visitor")
@Setter
@Getter
public class Visitor {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID visitorId;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String contactNumber;
}
