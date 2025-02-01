package com.example.demo5;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ReasonRepository extends JpaRepository<Reason, UUID> {
}
