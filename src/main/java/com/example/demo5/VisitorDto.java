package com.example.demo5;

import java.util.UUID;

public record VisitorDto(UUID visitorId, String firstName, String lastName, String contactNumber, Company company) {
}
