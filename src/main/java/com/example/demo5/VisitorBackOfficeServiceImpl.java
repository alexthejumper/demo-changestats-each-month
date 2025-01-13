package com.example.demo5;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.YearMonth;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class VisitorBackOfficeServiceImpl implements VisitorBackOfficeService {
    private final VisitorLogRepository visitorLogRepository;

    @Autowired
    public VisitorBackOfficeServiceImpl(VisitorLogRepository visitorLogRepository) {
        this.visitorLogRepository = visitorLogRepository;
    }
    private int calculateWeekInMonth(LocalDateTime clockIn) {
        return (clockIn.getDayOfMonth() - 1) / 7 + 1; // Calculate the week number within the month
    }

    @Override
    public List<VisitsCountDto> getVisitsCountPerWeek() {
        YearMonth currentYearMonth = YearMonth.now(); // Current year and month
        int currentYear = currentYearMonth.getYear();
        int currentMonth = currentYearMonth.getMonthValue();
        int currentWeek = (LocalDate.now().getDayOfMonth() - 1) / 7 + 1; // Current week in the month

        List<VisitorLog> visitorLogs = visitorLogRepository.findAll(); // Fetch all logs

        // Count visits per week in the current month and year
        Map<Integer, Long> weeklyVisitsMap = visitorLogs.stream()
                .filter(log -> {
                    LocalDateTime clockIn = log.getClockIn();
                    return clockIn != null && clockIn.getYear() == currentYear && clockIn.getMonthValue() == currentMonth;
                })
                .collect(Collectors.groupingBy(
                        log -> calculateWeekInMonth(log.getClockIn()), // Grouping by week in month
                        Collectors.counting() // Count occurrences
                ));

        // Prepare results for all weeks up to the current week
        return IntStream.rangeClosed(1, currentWeek)
                .mapToObj(week -> new VisitsCountDto(week, weeklyVisitsMap.getOrDefault(week, 0L)))
                .collect(Collectors.toList());
    }


    /*private int calculateWeekInMonth(LocalDateTime clockIn) {
        return (clockIn.getDayOfMonth() - 1) / 7 + 1; // Calculate the week number within the month
    }

    @Override
    public List<VisitsCountDto> getVisitsCountPerWeek() {
        List<VisitorLog> visitorLogs = Optional.of(visitorLogRepository.findAll()).orElse(Collections.emptyList());

        // Filter visitor logs for the specified year and month
        Map<Integer, Long> weeklyVisitsMap = visitorLogs.stream()
                .filter(visitorLog -> {
                    LocalDateTime clockIn = visitorLog.getClockIn();
                    return clockIn != null && clockIn.getYear() == 2024 && clockIn.getMonthValue() == 1;
                })
                .collect(Collectors.groupingBy(visitorLog -> calculateWeekInMonth(visitorLog.getClockIn()), Collectors.counting()));

        // Determine the total weeks in the specified month
        YearMonth yearMonth = YearMonth.of(2024, 1);
        int daysInMonth = yearMonth.lengthOfMonth();
        int totalWeeks = (daysInMonth + 6) / 7; // Calculate total weeks (ceiling division)

        // Create a complete list of weeks with default value 0 for weeks with no visits
        List<VisitsCountDto> result = new ArrayList<>();
        for (int week = 1; week <= totalWeeks; week++) {
            long totalVisits = weeklyVisitsMap.getOrDefault(week, 0L);
            result.add(new VisitsCountDto(week, totalVisits));
        }

        return result;
    }*/


    @Override
    public List<VisitorReasonCountDto> getCountOfReasons() {
        List<VisitorLog> visitorLogs = Optional.of(visitorLogRepository.findAll())
                .orElse(Collections.emptyList());

        Map<String, Long> reasonCounts = visitorLogs.stream()
                .collect(Collectors.groupingBy(
                        visitorLog -> Optional.ofNullable(visitorLog.getOtherReason())
                                .orElseGet(() -> Optional.ofNullable(visitorLog.getReason())
                                        .map(Reason::getReason)
                                        .orElse("Unknown reason")),
                        Collectors.counting()
                ));

        return reasonCounts.entrySet().stream()
                .map(entry -> new VisitorReasonCountDto(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }
}
