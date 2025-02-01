package com.example.demo5;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface VisitorLogRepository extends JpaRepository<VisitorLog, UUID> {

    boolean existsByBadge_BadgeIdAndClockOutIsNull(UUID badgeId);

    List<VisitorLog> findByClockOutIsNull();

    Optional<VisitorLog> findByBadge_BadgeIdAndClockOutIsNull(UUID badgeId);

    VisitorLog findByVisitor(Visitor visitor);
}
