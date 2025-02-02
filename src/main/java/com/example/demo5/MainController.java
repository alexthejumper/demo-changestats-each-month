package com.example.demo5;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

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

    @PostMapping("/updateTest")
    @ResponseBody
    public String updateAndCreateLogTest(@RequestBody VisitorTestDto testDto) {
        System.out.println("Test dto name: " + testDto.firstName());
        visitorPortalService.testUpdateAndCreate(testDto);
        return "Successful update and creation of log";
    }


    @GetMapping("/reasons/reasons-list")
    @ResponseBody
    public List<Reason> getAllReasons() {
        for (Reason reason : visitorBackOfficeService.getReasonTypeList()) {
            System.out.println("Reason: " + reason.isArchived());
        }
        return visitorBackOfficeService.getReasonTypeList();
    }

    @GetMapping("/contact")
    public ResponseEntity<Map<String, String>> getContactNumber(@RequestParam String firstName, @RequestParam String lastName) {
        String contactNumber = visitorPortalService.getContactNumber(firstName, lastName);

        if (contactNumber != null) {
            return ResponseEntity.ok(Collections.singletonMap("contactNumber", contactNumber));
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }
}

