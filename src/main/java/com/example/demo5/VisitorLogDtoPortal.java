package com.example.demo5;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record VisitorLogDtoPortal(UUID id, String otherReason, String attendeeName, LocalDateTime clockIn, LocalDateTime clockOut, String signature, Visitor visitor, Reason reason, Badge badge) {
}
