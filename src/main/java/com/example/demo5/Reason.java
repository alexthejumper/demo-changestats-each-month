package com.example.demo5;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
public class Reason {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID reasonId;

    @Column(nullable = false)
    private String reason;

    @Column
    private boolean archived;
}
