package com.example.demo5;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "visitor_log")
public class VisitorLog {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID visitorLogId;

    @Column
    private String otherReason;

    @Column
    private String attendeeName;

    @Column(nullable = false)
    private LocalDateTime clockIn;

    @Column
    private LocalDateTime clockOut;

    @Column(nullable = false)
    private String signature;

    @ManyToOne
    @JoinColumn(name = "visitor_id")
    private Visitor visitor;

    @ManyToOne
    @JoinColumn(name = "reason_id")
    private Reason reason;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "badge_id")
    private Badge badge;

}
