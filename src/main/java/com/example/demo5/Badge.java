package com.example.demo5;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
public class Badge {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID badgeId;

    @Column
    private String identification;

    @Column
    private String name;
}
