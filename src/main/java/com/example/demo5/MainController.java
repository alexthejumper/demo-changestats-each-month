package com.example.demo5;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/visitors")
public class MainController {

    private final VisitorBackOfficeService visitorBackOfficeService;
    private final VisitorPortalService visitorPortalService;

    @Autowired
    public MainController(VisitorBackOfficeService visitorBackOfficeService, VisitorPortalService visitorPortalService) {
        this.visitorBackOfficeService = visitorBackOfficeService;
        this.visitorPortalService = visitorPortalService;
    }

    @GetMapping("/visit-count")
    public ResponseEntity<List<VisitsCountDto>> getVisitsCountPerWeek() {
        List<VisitsCountDto> countWeeklyVisit = visitorBackOfficeService.getVisitsCountPerWeek();
        System.out.println(countWeeklyVisit.listIterator());
        return ResponseEntity.ok(countWeeklyVisit);
    }

    @GetMapping("/reason-count")
    public ResponseEntity<List<VisitorReasonCountDto>> getCountOfReasons() {
        List<VisitorReasonCountDto> countByReason = visitorBackOfficeService.getCountOfReasons();
        System.out.println(countByReason.listIterator());
        return ResponseEntity.ok(countByReason);
    }

    @PutMapping("/existing-visitor")
    public ResponseEntity<Visitor> updateVisitor(@Valid @RequestBody VisitorDto visitorDto) {
        return ResponseEntity.ok(visitorPortalService.updateExistingVisitor(visitorDto));
    }

    @PostMapping("/existing-visitor-log")
    public ResponseEntity<VisitorLogDtoPortal> createVisitorLog(@RequestBody VisitorLogDtoPortal visitorLogDtoPortal) {
        VisitorLogDtoPortal createdVisitorLog = visitorPortalService.createExistingVisitor(visitorLogDtoPortal);
        return new ResponseEntity<>(createdVisitorLog, HttpStatus.CREATED);
    }

//    @GetMapping("/visitor-status")
//    public ResponseEntity<List<VisitorCountDto>> getVisitorCountPerWeek() {
//        List<VisitorCountDto> visitorStatus = visitorBackOfficeService.getVisitorCountPerWeek();
//        return ResponseEntity.ok(visitorStatus);
//    }

    @GetMapping("existing-visitor")
    public ResponseEntity<List<VisitorLogDtoPortal>> getVisitorsByDetails(@RequestParam String firstName, @RequestParam String lastName, @RequestParam String companyName) {
        List<VisitorLogDtoPortal> visitors = visitorPortalService.getVisitorsByDetails(firstName, lastName, companyName);

        System.out.println(visitors.isEmpty());
        System.out.println(visitors.listIterator());

        if (visitors.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(visitors);
    }


    @GetMapping("existing-visitors")
    public ResponseEntity<VisitorLogDtoPortal> updateVisitorAndCreateLog(@RequestBody UpdateVisitorLogRequestDto request) {
        VisitorLogDtoPortal visitorLog = visitorPortalService.updateVisitorAndCreateLog(request.visitorDto(), request.visitorLogDtoPortal());
        return ResponseEntity.ok(visitorLog);
    }
}

